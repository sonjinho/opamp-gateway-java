package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import java.util.List;

public record TempoSpanSetDTO(
        List<TempoSpanDTO> spans,
        Integer matched
) {
}
