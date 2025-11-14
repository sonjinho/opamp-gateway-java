package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchRangeQuery;

public record RequestLokiQueryRangeDTO(
        String query,
        Long limit,
        String start,
        String end,
        String since,
        String step,
        String interval,
        String direction
) {
    public static RequestLokiQueryRangeDTO from(LogSearchRangeQuery searchQuery) {
        return new RequestLokiQueryRangeDTO(
                searchQuery.query(),
                searchQuery.limit() != null ? searchQuery.limit() : 100,
                searchQuery.start(),
                searchQuery.end(),
                searchQuery.since(),
                searchQuery.step(),
                searchQuery.interval(),
                searchQuery.direction() != null ? searchQuery.direction() : "backward"
        );
    }
}
