package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import java.util.Map;

public record LokiStreamDTO(
        Map<String, String> stream
) {
}
