package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.configuration;

import io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(prefix = "telemetry.metric", name = "type", havingValue = "prometheus")
@Component
public class PrometheusClient implements PrometheusRestClient {

    private final ParameterizedTypeReference<PrometheusResponse<PrometheusResultData>> typeRef =
            new ParameterizedTypeReference<PrometheusResponse<PrometheusResultData>>() {
            };

    private final RestClient restClient;

    public PrometheusClient(@Qualifier("prometheusRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<PrometheusResponse<PrometheusResultData>> instantQuery(RequestInstantQueryDTO requestQuery) {
        PrometheusResponse<PrometheusResultData> response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/v1/query");
                    uriBuilder.queryParam("query", requestQuery.query());
                    if (requestQuery.time() != null) {
                        uriBuilder.queryParam("time", requestQuery.time());
                    }
                    if (requestQuery.timeout() != null) {
                        uriBuilder.queryParam("timeout", requestQuery.timeout());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(typeRef);
        if (response == null || response.data() == null || response.status().equals("false")) {
            return Optional.empty();
        }
        return Optional.of(response);
    }

    @Override
    public Optional<PrometheusResponse<PrometheusResultData>> rangeQuery(RequestRangeQueryDTO rangeQuery) {
        PrometheusResponse<PrometheusResultData> response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/v1/query_range");
                    uriBuilder.queryParam("query", rangeQuery.query());
                    uriBuilder.queryParam("start", rangeQuery.start());
                    uriBuilder.queryParam("end", rangeQuery.end());
                    uriBuilder.queryParam("step", rangeQuery.step());
                    if (rangeQuery.timeout() != null) {
                        uriBuilder.queryParam("timeout", rangeQuery.timeout());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(typeRef);
        if (response == null || response.data() == null || response.status().equals("false")) {
            return Optional.empty();
        }
        return Optional.of(response);
    }

    @Override
    public PrometheusStringResponse getLabelNames(String start, String end, String match) {
        final String path = "/api/v1/labels";
        var response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (start != null) {
                        uriBuilder.queryParam("start", start);
                    }
                    if (end != null) {
                        uriBuilder.queryParam("end", end);
                    }
                    if (match != null) {
                        uriBuilder.queryParam("match[]", match);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(PrometheusStringResponse.class);
        if (response == null) return new PrometheusStringResponse("false", List.of());
        else return response;
    }

    @Override
    public PrometheusStringResponse getLabelValues(String labelName, String start, String end, String match) {
        final String path = "/api/v1/label/{labelName}/values";
        var response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (start != null) {
                        uriBuilder.queryParam("start", start);
                    }
                    if (end != null) {
                        uriBuilder.queryParam("end", end);
                    }
                    if (match != null) {
                        uriBuilder.queryParam("match[]", match);
                    }
                    return uriBuilder.build(labelName);
                })
                .retrieve()
                .body(PrometheusStringResponse.class);
        if (response == null) return new PrometheusStringResponse("false", List.of());
        else return response;
    }
}
