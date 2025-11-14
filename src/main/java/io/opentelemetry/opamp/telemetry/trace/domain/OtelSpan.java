package io.opentelemetry.opamp.telemetry.trace.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @param traceId           bytes
 * @param spanId            bytes
 * @param parentSpanId      bytes
 * @param kind              enum
 * @param startTimeUnixNano fixed64
 * @param endTimeUnixNano   fixed64
 */
public record OtelSpan(@JsonProperty("traceId") String traceId, @JsonProperty("spanId") String spanId,
                       @JsonProperty("traceState") String traceState, @JsonProperty("parentSpanId") String parentSpanId,
                       @JsonProperty("flags") Integer flags, @JsonProperty("name") String name,
                       @JsonProperty("kind") String kind, @JsonProperty("startTimeUnixNano") Long startTimeUnixNano,
                       @JsonProperty("endTimeUnixNano") Long endTimeUnixNano,
                       @JsonProperty("attributes") List<OtelAttribute> attributes,
                       @JsonProperty("droppedAttributesCount") Integer droppedAttributesCount,
                       @JsonProperty("events") List<OtelEvent> events,
                       @JsonProperty("droppedEventsCount") Integer droppedEventsCount,
                       @JsonProperty("links") List<OtelLink> links,
                       @JsonProperty("droppedLinksCount") Integer droppedLinksCount,
                       @JsonProperty("status") OtelStatus status) {
}