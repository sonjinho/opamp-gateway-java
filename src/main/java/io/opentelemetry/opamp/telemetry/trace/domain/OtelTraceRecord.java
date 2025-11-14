package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OtelTraceRecord(@JsonProperty("resourceSpans") List<OtelResourceSpans> resourceSpans) {
}