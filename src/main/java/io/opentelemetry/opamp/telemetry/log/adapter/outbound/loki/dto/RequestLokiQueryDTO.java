package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchQuery;

public record RequestLokiQueryDTO(
        String query,
        Long limit,
        String time,
        String direction
) {
    public static RequestLokiQueryDTO from(LogSearchQuery searchQuery) {
        return new RequestLokiQueryDTO(
                searchQuery.query(),
                searchQuery.limit() != null ? searchQuery.limit() : 100,
                searchQuery.time() != null ? searchQuery.time() : null,
                searchQuery.direction() != null ? searchQuery.direction() : "backward"
        );
    }
}
