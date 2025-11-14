package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus;

public record LabelValueSearchQuery(String labelName, String start, String end, String match) {
}
