package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class OtelLink {
    @JsonProperty("traceId")
    String traceId; // bytes

    @JsonProperty("spanId")
    String spanId; // bytes

    @JsonProperty("traceState")
    String traceState;

    @JsonProperty("attributes")
    List<OtelAttribute> attributes;

    @JsonProperty("droppedAttributesCount")
    Integer droppedAttributesCount;

    @JsonProperty("flags")
    Integer flags;
}
