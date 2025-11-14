package io.opentelemetry.opamp.telemetry.metric.application.port;

import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.LabelSearchQuery;
import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.LabelValueSearchQuery;
import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;

public interface MetricQueryPort {
    List<OtelMetricRecord> instantQuery(InstantSearchQuery searchQuery);

    List<OtelMetricRecord> rangeQuery(RangeSearchQuery searchQuery);

    List<String> queryLabels(LabelSearchQuery searchQuery);

    List<String> queryLabelValues(LabelValueSearchQuery searchQuery);
}
