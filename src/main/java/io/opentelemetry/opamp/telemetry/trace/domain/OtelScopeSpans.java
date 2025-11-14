package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OtelScopeSpans(@JsonProperty("scope") OtelInstrumentationScope scope,
                             @JsonProperty("spans") List<OtelSpan> spans, @JsonProperty("schemaUrl") String schemaUrl) {
}