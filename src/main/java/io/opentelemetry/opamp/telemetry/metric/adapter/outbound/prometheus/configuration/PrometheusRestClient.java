package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.configuration;

import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto.*;

import java.util.Optional;

public interface PrometheusRestClient {

    Optional<PrometheusResponse<PrometheusResultData>> instantQuery(RequestInstantQueryDTO requestQuery);

    Optional<PrometheusResponse<PrometheusResultData>> rangeQuery(RequestRangeQueryDTO rangeQuery);

    PrometheusStringResponse getLabelNames(String start, String end, String match);

    PrometheusStringResponse getLabelValues(String labelName, String start, String end, String match);

}