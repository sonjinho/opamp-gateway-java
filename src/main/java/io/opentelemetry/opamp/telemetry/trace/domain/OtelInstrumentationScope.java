package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OtelInstrumentationScope(@JsonProperty("name") String name, @JsonProperty("version") String version,
                                       @JsonProperty("attributes") List<OtelAttribute> attributes,
                                       @JsonProperty("dropped_attributes_count") int droppedAttributesCount) {
}