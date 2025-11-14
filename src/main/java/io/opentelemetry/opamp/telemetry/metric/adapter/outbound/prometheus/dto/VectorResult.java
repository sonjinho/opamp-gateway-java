package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import java.util.List;
import java.util.Map;

public record VectorResult(Map<String, String> metric, List<Object> value) {
}
