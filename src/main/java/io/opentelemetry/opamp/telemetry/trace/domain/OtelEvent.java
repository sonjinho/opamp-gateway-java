package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class OtelEvent {
    @JsonProperty("timeUnixNano")
    Long timeUnixNano;

    @JsonProperty("name")
    String name;

    @JsonProperty("attributes")
    List<OtelAttribute> attributes;

    @JsonProperty("droppedAttributesCount")
    Integer droppedAttributesCount;
}
