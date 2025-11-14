package io.opentelemetry.opamp.telemetry.metric.application.port;

public record RangeSearchQuery(
        String query, String start, String end, String step, String timeout, Integer limit

) {
}
