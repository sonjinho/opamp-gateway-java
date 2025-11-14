package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;


import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;
import java.util.Map;

public record StringData(
        String resultType,
        List<Object> value
) implements PrometheusResultData {
    public List<OtelMetricRecord> toMetricRecord() {

        long ts = ((Number) value.get(0)).longValue() * 1000;
        String str = (String) value.get(1);

        double numericValue = str.length();

        OtelMetricRecord.DataPoint dp =
                new OtelMetricRecord.DataPoint(
                        java.time.Instant.ofEpochMilli(ts),
                        numericValue
                );

        return List.of(
                new OtelMetricRecord(
                        "string_metric",
                        Map.of("original", str),   // 원본 문자열은 attribute에 보관
                        List.of(dp),
                        OtelMetricRecord.MetricType.GAUGE
                )
        );
    }
}
