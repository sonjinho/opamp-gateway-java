package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OtelResourceSpans(@JsonProperty("resource") OtelResource resource,
                                @JsonProperty("scopeSpans") List<OtelScopeSpans> scopeSpans,
                                @JsonProperty("schemaUrl") String schemaUrl) {
}
