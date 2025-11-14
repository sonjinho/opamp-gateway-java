package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtelAttribute(@JsonProperty("key") String key, @JsonProperty("value") OtelValue value) {
}
