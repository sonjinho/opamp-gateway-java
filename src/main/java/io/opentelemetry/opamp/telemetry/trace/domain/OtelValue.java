package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @param bytesValue Base64 encoded bytes
 */
public record OtelValue(@JsonProperty("stringValue") String stringValue, @JsonProperty("boolValue") Boolean boolValue,
                        @JsonProperty("intValue") Long intValue, @JsonProperty("doubleValue") Double doubleValue,
                        @JsonProperty("arrayValue") List<OtelValue> arrayValue,
                        @JsonProperty("kvlistValue") List<OtelAttribute> kvlistValue,
                        @JsonProperty("bytesValue") String bytesValue) {
}
