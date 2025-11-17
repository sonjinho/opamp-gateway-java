package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.configuration;

import com.google.protobuf.InvalidProtocolBufferException;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.TempoTagNamesQuery;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.TempoTagValuesQuery;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchQueryDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchResponseDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchTagValueResponseDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchTageNameResponseDTO;
import io.opentelemetry.proto.trace.v1.TracesData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "telemetry.trace", name = "type", havingValue = "tempo")
public class TempoClient implements TempoRestClient {

    private final RestClient restClient;

    public TempoClient(@Qualifier("tempoRestClient") RestClient tempoRestClient) {
        this.restClient = tempoRestClient;
    }

    @Override
    public Optional<TracesData> getTrace(String traceId) {
        final String path = "/api/v2/traces/{traceID}";
        byte[] response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).build(traceId))
                .header(HttpHeaders.ACCEPT, "application/protobuf")
                .retrieve()
                .body(byte[].class);

        try {
            return Optional.ofNullable(TracesData.parseFrom(response));
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public TempoSearchResponseDTO searchTraces(TempoSearchQueryDTO query) {
        final String path = "/api/search";
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (Objects.nonNull(query.query()) && !query.query().isBlank()) {
                        uriBuilder.queryParam("q", query.query());
                    } else {
                        if (query.tags() != null) {
                            uriBuilder.queryParam("tags", query.tags());
                        }
                        if (query.minDuration() != null) {
                            uriBuilder.queryParam("minDuration", query.minDuration());
                        }
                        if (query.maxDuration() != null) {
                            uriBuilder.queryParam("maxDuration", query.maxDuration());
                        }
                    }

                    if (query.limit() != null) {
                        uriBuilder.queryParam("limit", query.limit());
                    }
                    if (query.start() != null) {
                        uriBuilder.queryParam("start", query.start());
                    }
                    if (query.end() != null) {
                        uriBuilder.queryParam("end", query.end());
                    }
                    if (query.spss() != null) {
                        uriBuilder.queryParam("spss", query.spss());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(TempoSearchResponseDTO.class);
    }

    @Override
    public TempoSearchTageNameResponseDTO getTagNames(TempoTagNamesQuery query) {
        final String path = "/api/v2/search/tags";
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (query.scope() != null) {
                        uriBuilder.queryParam("scope", query.scope());
                    }
                    if (query.q() != null) {
                        uriBuilder.queryParam("q", query.q());
                    }
                    if (query.start() != null) {
                        uriBuilder.queryParam("start", query.start());
                    }
                    if (query.end() != null) {
                        uriBuilder.queryParam("end", query.end());
                    }
                    if (query.limit() != null) {
                        uriBuilder.queryParam("limit", query.limit());
                    }
                    if (query.maxStaleValues() != null) {
                        uriBuilder.queryParam("maxStaleValues", query.maxStaleValues());
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(TempoSearchTageNameResponseDTO.class);
    }

    @Override
    public TempoSearchTagValueResponseDTO getTagValues(String tagName, TempoTagValuesQuery query) {
        final String path = "/api/v2/search/tag/{tag_name}/values";
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (query.start() != null) {
                        uriBuilder.queryParam("start", query.start());
                    }
                    if (query.end() != null) {
                        uriBuilder.queryParam("end", query.end());
                    }
                    if (query.q() != null) {
                        uriBuilder.queryParam("q", query.q());
                    }
                    if (query.limit() != null) {
                        uriBuilder.queryParam("limit", query.limit());
                    }
                    if (query.maxStaleValues() != null) {
                        uriBuilder.queryParam("maxStaleValues", query.maxStaleValues());
                    }
                    return uriBuilder.build(tagName);
                })
                .retrieve()
                .body(TempoSearchTagValueResponseDTO.class);
    }

}
