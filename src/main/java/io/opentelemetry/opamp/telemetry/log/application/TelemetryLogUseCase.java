package io.opentelemetry.opamp.telemetry.log.application;

import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchQuery;
import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchRangeQuery;
import io.opentelemetry.opamp.telemetry.log.domain.OtelLogRecord;

import java.util.List;

public interface TelemetryLogUseCase {
    List<OtelLogRecord> query(LogSearchQuery searchQuery);

    List<OtelLogRecord> queryRange(LogSearchRangeQuery searchQuery);

    List<String> queryLabels();

    List<String> queryLabelValues(String label);
}
