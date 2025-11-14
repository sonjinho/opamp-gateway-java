package io.opentelemetry.opamp.telemetry.log.application.service;

import io.opentelemetry.opamp.telemetry.log.application.TelemetryLogUseCase;
import io.opentelemetry.opamp.telemetry.log.application.port.LogQueryPort;
import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchQuery;
import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchRangeQuery;
import io.opentelemetry.opamp.telemetry.log.domain.OtelLogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelemetryLogService implements TelemetryLogUseCase {

    private final LogQueryPort logQueryPort;

    @Override
    public List<OtelLogRecord> query(LogSearchQuery searchQuery) {
        return logQueryPort.query(searchQuery);
    }

    @Override
    public List<OtelLogRecord> queryRange(LogSearchRangeQuery searchQuery) {
        return logQueryPort.queryRange(searchQuery);
    }

    @Override
    public List<String> queryLabels() {
        return logQueryPort.queryLabels();
    }

    @Cacheable(value = "logLabelValues", key = "#label")
    @Override
    public List<String> queryLabelValues(String label) {
        return logQueryPort.queryLabelValues(label);
    }
}
