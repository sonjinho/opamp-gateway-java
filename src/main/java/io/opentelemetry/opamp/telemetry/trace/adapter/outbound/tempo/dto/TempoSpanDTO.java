package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import java.util.List;

public record TempoSpanDTO(String spanID, String startTimeUnixNano, String durationNanos,
                           List<TempoAttributeDTO> attributes) {
}
