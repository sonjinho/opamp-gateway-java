package io.opentelemetry.opamp.telemetry.log.domain;

import java.time.Instant;
import java.util.Map;


public record OtelLogRecord(
        Instant timestamp,
        String severityText,
        String body,
        Map<String, String> attributes,
        Map<String, String> resource,
        String traceId,
        String spanId
) {
}
