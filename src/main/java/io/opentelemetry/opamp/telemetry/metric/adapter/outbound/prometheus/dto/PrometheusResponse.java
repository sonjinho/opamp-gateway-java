package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;

public record PrometheusResponse<T extends PrometheusResultData>(
        @JsonProperty("status") String status,
        @JsonProperty("data") T data,
        String errorType,
        String error
) {
    public List<OtelMetricRecord> toEntries() {
        if (data == null) return List.of();
        return data.toMetricRecord();
    }
}
