package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import io.opentelemetry.opamp.telemetry.metric.application.port.InstantSearchQuery;

public record RequestInstantQueryDTO(
        String query, String time, String timeout, Integer limit
) {
    public static RequestInstantQueryDTO from(InstantSearchQuery searchQuery) {
        return new RequestInstantQueryDTO(
                searchQuery.query(),
                searchQuery.time(),
                searchQuery.timeout(),
                searchQuery.limit()
        );
    }
}
