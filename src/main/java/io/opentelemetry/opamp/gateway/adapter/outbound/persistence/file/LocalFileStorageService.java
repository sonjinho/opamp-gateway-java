package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Component
@Slf4j
public class LocalFileStorageService implements FileStorageService {

    private static final int BATCH_SIZE = 500;
    private static final long FLUSH_INTERVAL_MS = 2000;
    private final Path basePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<UUID, BlockingQueue<WriteTask>> queues = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, AgentToServerDomain> memoryCache = new ConcurrentHashMap<>();
    private final ExecutorService writerExecutor = Executors.newFixedThreadPool(4);
    private final ScheduledExecutorService flusher = Executors.newSingleThreadScheduledExecutor();

    public LocalFileStorageService(FileStorageProperties props) {
        this.basePath = props.getBasePath();
    }

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 주기적으로 flush
        flusher.scheduleAtFixedRate(this::flushAll, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);

        // Writer 쓰레드 실행
        writerExecutor.submit(() -> {
            while (true) {
                for (var entry : queues.entrySet()) {
                    var queue = entry.getValue();
                    List<WriteTask> batch = new ArrayList<>();
                    queue.drainTo(batch, BATCH_SIZE);
                    if (!batch.isEmpty()) writeBatch(entry.getKey(), batch);
                }
                Thread.sleep(50);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(this::flushAll));
    }

    @Override
    public void save(AgentToServerDomain domain) {
        try {
            UUID instanceUid = domain.instanceId();
            long seqNum = domain.seqNum();
            byte[] data = objectMapper.writeValueAsBytes(domain);
            queues.computeIfAbsent(instanceUid, id -> new LinkedBlockingQueue<>())
                    .offer(new WriteTask(seqNum, data));

            // 최신 데이터는 메모리에 보관
            memoryCache.put(instanceUid, domain);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBatch(UUID instanceUid, List<WriteTask> batch) {
        try {
            Path blockDir = basePath.resolve(LocalDate.now().toString());
            Path chunkDir = blockDir.resolve("chunks");
            Files.createDirectories(chunkDir);

            Path chunkFile = chunkDir.resolve(instanceUid + ".jsonl");
            Path indexFile = blockDir.resolve("index.json");

            // 인덱스 로드
            Map<String, FileIndexEntry> index = loadIndex(indexFile);
            FileIndexEntry meta = index.getOrDefault(instanceUid.toString(),
                    new FileIndexEntry(0, 0));

            long startSeq = meta.startSeq() == 0 ? batch.get(0).seqNum() : meta.startSeq();
            long endSeq = batch.get(batch.size() - 1).seqNum();

            // append write
            try (BufferedWriter writer = Files.newBufferedWriter(chunkFile,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                for (WriteTask t : batch) {
                    writer.write(new String(t.data(), StandardCharsets.UTF_8));
                    writer.newLine();
                }
            }

            // 인덱스 갱신
            index.put(instanceUid.toString(), new FileIndexEntry(startSeq, endSeq));
            saveIndex(indexFile, index);

        } catch (Exception e) {
            log.error("Failed to write batch for {}: {}", instanceUid, e.getMessage(), e);
        }
    }

    private Map<String, FileIndexEntry> loadIndex(Path file) {
        if (!Files.exists(file)) return new ConcurrentHashMap<>();
        try {
            String json = Files.readString(file);
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.warn("Failed to load index.json: {}", e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveIndex(Path file, Map<String, FileIndexEntry> index) {
        try {
            Files.createDirectories(file.getParent());
            Path tmp = Files.createTempFile(file.getParent(), "index-", ".tmp");
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(index);
            Files.writeString(tmp, json, StandardCharsets.UTF_8);

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
                try (FileLock lock = channel.lock()) {
                    Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                }
            }
        } catch (IOException e) {
            log.warn("Failed to save index.json: {}", e.getMessage());
        }
    }


    private void flushAll() {
        for (UUID uid : queues.keySet()) {
            var queue = queues.get(uid);
            if (queue == null || queue.isEmpty()) continue;
            List<WriteTask> batch = new ArrayList<>();
            queue.drainTo(batch, BATCH_SIZE);
            if (!batch.isEmpty()) writeBatch(uid, batch);
        }
    }

    // 🔹 최근 데이터
    @Override
    public AgentToServerDomain read(UUID instanceUid) {
        AgentToServerDomain cache = memoryCache.get(instanceUid);
        if (cache != null) return cache;

        // fallback: 파일에서 읽기
        Path chunkFile = basePath.resolve(LocalDate.now().toString())
                .resolve("chunks")
                .resolve(instanceUid + ".jsonl");

        if (!Files.exists(chunkFile)) return null;
        try (Stream<String> lines = Files.lines(chunkFile)) {
            return objectMapper.readValue(
                    lines.reduce((a, b) -> b).orElse("{}")
                            .getBytes(StandardCharsets.UTF_8), AgentToServerDomain.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 🔹 특정 seqNum
    @Override
    public AgentToServerDomain read(UUID instanceUid, long seqNum) {
        try {
            Path indexFile = basePath.resolve(LocalDate.now().toString()).resolve("index.json");
            Map<String, FileIndexEntry> index = loadIndex(indexFile);
            FileIndexEntry entry = index.get(instanceUid.toString());
            if (entry == null || seqNum < entry.startSeq() || seqNum > entry.endSeq()) {
                return null;
            }

            Path chunkFile = basePath.resolve(LocalDate.now().toString())
                    .resolve("chunks")
                    .resolve(instanceUid + ".jsonl");

            try (Stream<String> lines = Files.lines(chunkFile)) {
                return objectMapper.readValue(lines.filter(l -> l.contains("\"seqNum\":" + seqNum))
                        .findFirst()
                        .map(s -> s.getBytes(StandardCharsets.UTF_8))
                        .orElse(new byte[0]), AgentToServerDomain.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 🔹 전체 읽기
    @Override
    public List<AgentToServerDomain> readAll(UUID instanceUid) {
        try {
            Path chunkFile = basePath.resolve(LocalDate.now().toString())
                    .resolve("chunks")
                    .resolve(instanceUid + ".jsonl");

            if (!Files.exists(chunkFile)) return List.of();

            List<AgentToServerDomain> list = new ArrayList<>();
            try (ReversedLinesFileReader reader = ReversedLinesFileReader.builder().setFile(chunkFile.toFile()).setCharset(StandardCharsets.UTF_8).get()) {
                int count = 0;
                String line;
                while ((line = reader.readLine()) != null && count < 100) {
                    list.add(objectMapper.readValue(line, AgentToServerDomain.class));
                    count++;
                }
            }
            Collections.reverse(list);
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record WriteTask(long seqNum, byte[] data) {
    }

    private record FileIndexEntry(long startSeq, long endSeq) {
    }
}