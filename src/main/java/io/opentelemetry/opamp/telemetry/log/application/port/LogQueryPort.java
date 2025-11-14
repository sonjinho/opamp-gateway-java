package io.opentelemetry.opamp.telemetry.log.application.port;

import io.opentelemetry.opamp.telemetry.log.domain.OtelLogRecord;

import java.util.List;

public interface LogQueryPort {
    List<OtelLogRecord> query(LogSearchQuery searchQuery);

    List<OtelLogRecord> queryRange(LogSearchRangeQuery searchQuery);

    List<String> queryLabels();

    List<String> queryLabelValues(String label);
}
