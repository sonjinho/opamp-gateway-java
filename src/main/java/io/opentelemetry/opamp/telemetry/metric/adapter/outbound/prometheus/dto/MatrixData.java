package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;
import java.util.Map;

public record MatrixData(
        String resultType,
        List<MatrixResult> result
) implements PrometheusResultData {
    public List<OtelMetricRecord> toMetricRecord() {

        return result.stream().map(r -> {

            String metricName = r.metric().getOrDefault("__name__", "unknown_metric");
            Map<String, String> attributes = r.metric();

            // 각 series의 values = [[timestamp, value], ...]
            List<OtelMetricRecord.DataPoint> dataPoints =
                    r.values().stream().map(v -> {

                        long ts = ((Number) v.get(0)).longValue() * 1000;
                        double value = Double.parseDouble((String) v.get(1));

                        return new OtelMetricRecord.DataPoint(
                                java.time.Instant.ofEpochMilli(ts),
                                value
                        );

                    }).toList();

            OtelMetricRecord.MetricType type = inferMetricType(metricName);

            return new OtelMetricRecord(
                    metricName,
                    attributes,
                    dataPoints,
                    type
            );

        }).toList();
    }

    private OtelMetricRecord.MetricType inferMetricType(String name) {
        if (name.endsWith("_total") || name.endsWith("_sum")) return OtelMetricRecord.MetricType.COUNTER;
        if (name.endsWith("_count") || name.endsWith("_bucket")) return OtelMetricRecord.MetricType.HISTOGRAM;
        return OtelMetricRecord.MetricType.GAUGE;
    }
}
