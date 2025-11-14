package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class OtelResource {
    @JsonProperty("attributes")
    List<OtelAttribute> attributes;
}
