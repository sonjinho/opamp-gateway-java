package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus;

import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.configuration.PrometheusRestClient;
import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto.PrometheusResponse;
import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto.RequestInstantQueryDTO;
import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto.RequestRangeQueryDTO;
import io.opentelemetry.opamp.telemetry.metric.application.port.InstantSearchQuery;
import io.opentelemetry.opamp.telemetry.metric.application.port.MetricQueryPort;
import io.opentelemetry.opamp.telemetry.metric.application.port.RangeSearchQuery;
import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrometheusMetricQueryAdapter implements MetricQueryPort {

    private final PrometheusRestClient prometheusRestClient;

    @Override
    public List<OtelMetricRecord> instantQuery(InstantSearchQuery searchQuery) {
        return prometheusRestClient.instantQuery(RequestInstantQueryDTO.from(searchQuery)).map(PrometheusResponse::toEntries).orElse(List.of());
    }

    @Override
    public List<OtelMetricRecord> rangeQuery(RangeSearchQuery searchQuery) {
        return prometheusRestClient.rangeQuery(RequestRangeQueryDTO.from(searchQuery)).map(PrometheusResponse::toEntries).orElse(List.of());
    }

    @Override
    public List<String> queryLabels(LabelSearchQuery searchQuery) {
        return prometheusRestClient.getLabelNames(searchQuery.start(), searchQuery.end(), searchQuery.match()).data();
    }

    @Override
    public List<String> queryLabelValues(LabelValueSearchQuery searchQuery) {
        return prometheusRestClient.getLabelValues(searchQuery.labelName(), searchQuery.start(), searchQuery.end(), searchQuery.match()).data();
    }
}
