package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import java.util.List;

public record LokiStringResponseDTO(
        String status,
        List<String> data
) {
}
