package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class OtelStatus {
    @JsonProperty("message")
    String message;

    @JsonProperty("code")
    String code; // This should be an enum, but for simplicity, I'll use String for now.
}
