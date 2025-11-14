package io.opentelemetry.opamp.telemetry.metric.application.port;

public record InstantSearchQuery(String query, String time, String timeout, Integer limit) {
}
