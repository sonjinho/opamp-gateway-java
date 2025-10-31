package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Slf4j
@ConditionalOnProperty(name = "request.persistence.type", havingValue = "FILE")
@Component
public class LocalFileStorageService implements FileStorageService {
    private static final int BATCH_SIZE = 500;
    private static final long FLUSH_INTERVAL_MS = 10000;
    private final Path basePath;
    private final ObjectMapper objectMapper;

    // index
    private final NavigableMap<LocalDate, ConcurrentHashMap<UUID, FileIndexEntry>> inMemoryIndexes = new ConcurrentSkipListMap<>();
    private final ExecutorService indexWriterExecutor = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<UUID, AgentToServerDomain> memoryCache = new ConcurrentHashMap<>();
    private final TypeReference<Map<UUID, FileIndexEntry>> TYPE_REF = new TypeReference<>() {
    };

    // chunk
    private final ConcurrentHashMap<UUID, BlockingQueue<WriteTask>> queues = new ConcurrentHashMap<>();
    private final ExecutorService writerChunkExecutor = Executors.newFixedThreadPool(4);
    private final ScheduledExecutorService flusher = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public LocalFileStorageService(FileStorageProperties props, ObjectMapper mapper) {
        this.basePath = props.getBasePath();
        this.objectMapper = mapper;
    }

    @PostConstruct
    public void init() {
        log.info("Loading all existing indices from {}", basePath);

        try (Stream<Path> stream = Files.list(this.basePath)) {
            stream.filter(Files::isDirectory)
                    .forEach(dir -> {
                        LocalDate dateKey = LocalDate.parse(dir.getFileName().toString());
                        Path indexFile = dir.resolve("index.json");

                        if (Files.exists(indexFile)) {
                            try {
                                Map<UUID, FileIndexEntry> loadedIndex = objectMapper.readValue(Files.readString(indexFile), TYPE_REF);
                                inMemoryIndexes.put(dateKey, new ConcurrentHashMap<>(loadedIndex));
                                log.info("Loaded index for date: {} with {} entries.", dateKey, loadedIndex.size());
                            } catch (IOException e) {
                                log.error("Failed to load index file {}: {}", indexFile, e.getMessage());
                            }
                        }

                    });
        } catch (IOException e) {
            log.error("Failed to load existing indices on startup, {}", e.getMessage());
        }

        flusher.scheduleAtFixedRate(this::flushAll, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);

        writerChunkExecutor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    for (var entry : queues.entrySet()) {
                        var queue = entry.getValue();
                        var batch = new ArrayList<WriteTask>();
                        queue.drainTo(batch, BATCH_SIZE);
                        if (!batch.isEmpty()) {
                            writeBatch(entry.getKey(), batch);
                        }
                    }
                    Thread.sleep(50);
                }
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
                log.warn("Writer Executor interrupted.");
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Running shutdown hook: flushing and saving index.");
            flushAll();
        }));
    }


    @PreDestroy
    public void destroy() {
        flushAll();
        writerChunkExecutor.shutdown();
        indexWriterExecutor.shutdown();
        flusher.shutdown();
    }

    @Override
    public void save(AgentToServerDomain domain) {
        UUID instanceId = domain.instanceId();
        long seqNum = domain.seqNum();
        boolean offer = queues.computeIfAbsent(instanceId, id -> new LinkedBlockingQueue<>()).offer(new WriteTask(seqNum, domain));
        if (offer) {
            memoryCache.put(instanceId, domain);
        }
    }

    @Override
    public AgentToServerDomain read(UUID instanceUid) {
        AgentToServerDomain cache = memoryCache.get(instanceUid);
        if (cache != null) return cache;

        for (var entry : inMemoryIndexes.descendingMap().entrySet()) {
            var dayIndex = entry.getValue();
            if (dayIndex.containsKey(instanceUid)) {
                return findLastByDate(entry.getKey(), instanceUid);
            }
        }
        return null;
    }

    private AgentToServerDomain findLastByDate(LocalDate date, UUID instanceUid) {
        Path chunkFile = basePath.resolve(date.toString())
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

    @Override
    public AgentToServerDomain read(UUID instanceUid, long seqNum) {
        for (Map.Entry<LocalDate, ConcurrentHashMap<UUID, FileIndexEntry>> entry : inMemoryIndexes.entrySet()) {
            LocalDate dateKey = entry.getKey();
            Map<UUID, FileIndexEntry> dayIndex = entry.getValue();
            FileIndexEntry index = dayIndex.get(instanceUid);
            if (index == null || seqNum < index.startSeq() || seqNum > index.endSeq()) {
                continue;
            }
            Path chunkFile = basePath.resolve(dateKey.toString())
                    .resolve("chunks")
                    .resolve(instanceUid + ".jsonl");

            try (Stream<String> lines = Files.lines(chunkFile)) {
                Optional<byte[]> data = lines.filter(l -> l.contains("\"seqNum\":" + seqNum))
                        .findFirst()
                        .map(s -> s.getBytes(StandardCharsets.UTF_8));
                if (data.isEmpty()) return null;
                return objectMapper.readValue(data.get(), AgentToServerDomain.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

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

    private void writeBatch(UUID key, List<WriteTask> batch) {
        try {
            LocalDate dateKey = LocalDate.now();
            Path blockDir = basePath.resolve(dateKey.toString());
            Path chunkDir = blockDir.resolve("chunks");
            Files.createDirectories(chunkDir);

            Path chunkFile = chunkDir.resolve(key + ".jsonl");

            ConcurrentHashMap<UUID, FileIndexEntry> dayIndex = inMemoryIndexes.computeIfAbsent(dateKey, k -> new ConcurrentHashMap<>());

            FileIndexEntry meta = dayIndex.getOrDefault(key, new FileIndexEntry(0, 0));

            long startSeq = meta.startSeq() == 0 ? batch.get(0).seqNum() : meta.startSeq();
            long endSeq = batch.get(batch.size() - 1).seqNum();

            indexWriterExecutor.submit(() -> {
                ConcurrentMap<UUID, FileIndexEntry> indexToUpdate = inMemoryIndexes.computeIfAbsent(dateKey,
                        k -> new ConcurrentHashMap<>());
                indexToUpdate.put(key, new FileIndexEntry(startSeq, endSeq));
            });

            try (BufferedWriter writer = Files.newBufferedWriter(chunkFile,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                for (WriteTask task : batch) {
                    writer.write(objectMapper.writeValueAsString(task.domain));
                    writer.newLine();
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAllIndexesToDisk() {
        try {
            indexWriterExecutor.submit(() -> {
                // save today, and yesterday
                LocalDate today = LocalDate.now();
                LocalDate yesterday = LocalDate.now().minusDays(1);
                for (LocalDate dateKey : List.of(today, yesterday)) {
                    var dayIndex = inMemoryIndexes.get(dateKey);
                    if (dayIndex.isEmpty()) continue;
                    Path indexFile = basePath.resolve(dateKey.toString()).resolve("index.json");
                    try {
                        Files.writeString(indexFile, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dayIndex));
                    } catch (IOException e) {
                        log.error("Critical: Failed to save in-memory index to disk for date: {}", dateKey, e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Failed to schedule index save to disk.", e);
        }
    }

    public void flushAll() {
        queues.forEach((uid, queue) -> {
            if (queue == null || queue.isEmpty()) return;
            List<WriteTask> batch = new ArrayList<>();
            queue.drainTo(batch, BATCH_SIZE);
            if (!batch.isEmpty()) writerChunkExecutor.submit(() -> writeBatch(uid, batch));
        });
        indexWriterExecutor.submit(this::saveAllIndexesToDisk);
    }

    private record WriteTask(long seqNum, AgentToServerDomain domain) {
    }

    private record FileIndexEntry(long startSeq, long endSeq) {
    }
}