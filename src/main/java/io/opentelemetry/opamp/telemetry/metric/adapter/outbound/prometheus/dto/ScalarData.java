package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;
import java.util.Map;

public record ScalarData(
        String resultType,
        List<Object> value
) implements PrometheusResultData {
    public List<OtelMetricRecord> toMetricRecord() {

        long ts = ((Number) value.get(0)).longValue() * 1000;
        double v = Double.parseDouble((String) value.get(1));

        OtelMetricRecord.DataPoint dp =
                new OtelMetricRecord.DataPoint(
                        java.time.Instant.ofEpochMilli(ts),
                        v
                );

        // metric 이름이 없음 → 기본 이름
        String metricName = "scalar_metric";

        return List.of(
                new OtelMetricRecord(
                        metricName,
                        Map.of(),               // scalar에는 label이 없음
                        List.of(dp),
                        OtelMetricRecord.MetricType.GAUGE   // scalar는 gauge로 보는 것이 가장 자연스러움
                )
        );
    }
}
