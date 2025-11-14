package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.configuration;

import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiStringResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryRangeDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(prefix = "telemetry.log", name = "type", havingValue = "loki")
@Component
public class LokiClient implements LokiRestClient {

    private final ParameterizedTypeReference<List<String>> typeRef =
            new ParameterizedTypeReference<List<String>>() {
            };
    private final RestClient restClient;

    public LokiClient(@Qualifier("lokiRestClient") RestClient lokiRestClient) {
        this.restClient = lokiRestClient;
    }

    @Override
    public Optional<LokiResponseDTO> query(RequestLokiQueryDTO requestQuery) {
        final String path = "/loki/api/v1/query";
        LokiResponseDTO response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    uriBuilder.queryParam("query", requestQuery.query());
                    if (requestQuery.limit() != null) {
                        uriBuilder.queryParam("limit", requestQuery.limit());
                    }
                    if (requestQuery.time() != null) {
                        uriBuilder.queryParam("time", requestQuery.time());
                    }
                    if (requestQuery.direction() != null) {
                        uriBuilder.queryParam("direction", requestQuery.direction());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(LokiResponseDTO.class);

        if (response == null || response.data() == null || response.data().result() == null) {
            return Optional.empty();
        }

        return Optional.of(response);
    }

    @Override
    public Optional<LokiResponseDTO> queryRange(RequestLokiQueryRangeDTO requestQueryRange) {
        final String path = "/loki/api/v1/query_range";
        LokiResponseDTO response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    uriBuilder.queryParam("query", requestQueryRange.query());
                    if (requestQueryRange.limit() != null) {
                        uriBuilder.queryParam("limit", requestQueryRange.limit());
                    }
                    if (requestQueryRange.start() != null) {
                        uriBuilder.queryParam("start", requestQueryRange.start());
                    }
                    if (requestQueryRange.end() != null) {
                        uriBuilder.queryParam("end", requestQueryRange.end());
                    }
                    if (requestQueryRange.since() != null) {
                        uriBuilder.queryParam("since", requestQueryRange.since());
                    }
                    if (requestQueryRange.step() != null) {
                        uriBuilder.queryParam("step", requestQueryRange.step());
                    }
                    if (requestQueryRange.interval() != null) {
                        uriBuilder.queryParam("interval", requestQueryRange.interval());
                    }
                    if (requestQueryRange.direction() != null) {
                        uriBuilder.queryParam("direction", requestQueryRange.direction());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(LokiResponseDTO.class);

        if (response == null || response.data() == null || response.data().result() == null) {
            return Optional.empty();
        }

        return Optional.of(response);
    }

    @Override
    public LokiStringResponseDTO queryLabels() {
        final String path = "/loki/api/v1/labels";
        return restClient.get()
                .uri(path)
                .retrieve()
                .body(LokiStringResponseDTO.class);
    }

    @Override
    public LokiStringResponseDTO queryLabelValues(String label) {
        final String path = "/loki/api/v1/label/{label}/values";
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(label))
                .retrieve()
                .body(LokiStringResponseDTO.class);
    }
}
