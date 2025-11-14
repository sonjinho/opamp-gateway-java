package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import java.util.List;
import java.util.Map;

public record MatrixResult(Map<String, String> metric, List<List<Object>> values) {
}
