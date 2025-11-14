package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import io.opentelemetry.opamp.telemetry.metric.application.port.RangeSearchQuery;

public record RequestRangeQueryDTO(
        String query, String start, String end, String step, String timeout, Integer limit
) {
    public static RequestRangeQueryDTO from(RangeSearchQuery searchQuery) {
        return new RequestRangeQueryDTO(
                searchQuery.query(),
                searchQuery.start(),
                searchQuery.end(),
                searchQuery.step(),
                searchQuery.timeout(),
                searchQuery.limit()
        );
    }
}
