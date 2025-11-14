package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record VectorData(
        String resultType,
        List<VectorResult> result
) implements PrometheusResultData {

    public List<OtelMetricRecord> toMetricRecord() {
        return result.stream().map(r -> {
            String metricName = r.metric().getOrDefault("__name__", "unknown_metric");
            Map<String, String> attributes = r.metric();
            List<Object> val = r.value();
            long ts = ((Number) val.get(0)).longValue() * 1000;
            double value = Double.parseDouble((String) val.get(1));
            OtelMetricRecord.DataPoint dataPoint = new OtelMetricRecord.DataPoint(Instant.ofEpochMilli(ts), value);
            OtelMetricRecord.MetricType type = inferMetricType(metricName);
            return new OtelMetricRecord(
                    metricName,
                    attributes,
                    List.of(dataPoint),
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
