package io.opentelemetry.opamp.telemetry.metric.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record OtelMetricRecord(
        String name,
        Map<String, String> attributes,
        List<DataPoint> dataPoints,
        MetricType type
) {
    public enum MetricType {
        GAUGE,
        COUNTER,
        SUMMARY,
        HISTOGRAM,
        UNKNOWN
    }

    public record DataPoint(Instant timestamp, double value) {
    }
}
