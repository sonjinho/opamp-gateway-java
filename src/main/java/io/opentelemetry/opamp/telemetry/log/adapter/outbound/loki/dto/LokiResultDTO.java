package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import java.util.List;
import java.util.Map;

public record LokiResultDTO(
        Map<String, String> stream,
        List<List<String>> values
) {
}