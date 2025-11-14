package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import java.util.List;

public record LokiDataDTO(
        String resultType,
        List<LokiResultDTO> result
) {
}
