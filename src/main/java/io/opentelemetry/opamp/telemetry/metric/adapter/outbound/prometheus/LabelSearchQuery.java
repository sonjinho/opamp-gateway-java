package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus;

public record LabelSearchQuery(String start, String end, String match) {
}
