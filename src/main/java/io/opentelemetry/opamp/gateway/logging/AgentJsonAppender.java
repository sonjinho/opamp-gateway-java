package io.opentelemetry.opamp.gateway.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AgentJsonAppender extends AppenderBase<ILoggingEvent> {

    private final ObjectMapper objectMapper;
    private final Map<UUID, BufferedWriter> writerCache = new ConcurrentHashMap<>();

    @Setter
    private String allowedLogger =
            "io.opentelemetry.opamp.gateway.adapter.outbound.persistence.file.LogFileStorageService";

    @Setter
    private boolean immediateFlush = true; // 기본값 true
    @Setter
    private int flushInterval = 10;        // immediateFlush=false 일 때 n회마다 flush
    private int writeCount = 0;
    @Setter
    private String baseDir;

    public AgentJsonAppender() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!allowedLogger.equals(event.getLoggerName())) return;

        Object[] args = event.getArgumentArray();
        if (args == null || args.length == 0) return;
        Object firstArg = args[0];
        if (!(firstArg instanceof AgentToServerDomain domain)) return;

        writeToFile(domain);
    }

    private void writeToFile(AgentToServerDomain domain) {
        try {
            UUID instanceId = domain.instanceId();
            if (instanceId == null) return;

            BufferedWriter writer = writerCache.computeIfAbsent(instanceId, this::createWriter);
            String json = objectMapper.writeValueAsString(domain);
            writer.write(json);
            writer.newLine();

            if (immediateFlush) {
                writer.flush();
            } else if (++writeCount % flushInterval == 0) {
                writer.flush();
                writeCount = 0;
            }

        } catch (IOException e) {
            addError("Failed to write AgentToServerDomain log", e);
        }
    }

    private BufferedWriter createWriter(UUID instanceId) {
        try {
            String dateDir = LocalDate.now().toString();
            File dir = new File(baseDir, dateDir + "/chunks");
            if (!dir.exists() && !dir.mkdirs()) {
                addError("Failed to create directory: " + dir.getAbsolutePath());
                return null;
            }
            File file = new File(dir, instanceId + ".jsonl");
            return new BufferedWriter(new FileWriter(file, true), 8192);
        } catch (IOException e) {
            addError("Failed to create writer for " + instanceId, e);
            return null;
        }
    }

    @Override
    public void stop() {
        super.stop();
        // 모든 Writer flush 및 close
        writerCache.values().forEach(writer -> {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                addError("Failed to close writer", e);
            }
        });
        writerCache.clear();
    }
}
