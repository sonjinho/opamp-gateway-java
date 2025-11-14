package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import java.util.List;

public record PrometheusStringResponse(
        String status,
        List<String> data
) {
}
