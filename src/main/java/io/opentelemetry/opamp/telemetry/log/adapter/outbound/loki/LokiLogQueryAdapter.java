package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki;

import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.configuration.LokiRestClient;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiStringResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryRangeDTO;
import io.opentelemetry.opamp.telemetry.log.application.port.LogQueryPort;
import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchQuery;
import io.opentelemetry.opamp.telemetry.log.application.port.LogSearchRangeQuery;
import io.opentelemetry.opamp.telemetry.log.domain.OtelLogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(prefix = "telemetry.log", name = "type", havingValue = "loki")
@RequiredArgsConstructor
@Component
public class LokiLogQueryAdapter implements LogQueryPort {

    private final LokiRestClient lokiRestClient;

    @Override
    public List<OtelLogRecord> query(LogSearchQuery searchQuery) {
        return lokiRestClient.query(RequestLokiQueryDTO.from(searchQuery)).map(LokiResponseDTO::toEntries).orElse(List.of());
    }

    @Override
    public List<OtelLogRecord> queryRange(LogSearchRangeQuery searchQuery) {
        return lokiRestClient.queryRange(RequestLokiQueryRangeDTO.from(searchQuery)).map(LokiResponseDTO::toEntries).orElse(List.of());
    }

    @Override
    public List<String> queryLabels() {
        return Optional.ofNullable(lokiRestClient.queryLabels()).map(LokiStringResponseDTO::data).orElse(List.of());
    }

    @Override
    public List<String> queryLabelValues(String label) {
        return Optional.ofNullable(lokiRestClient.queryLabelValues(label)).map(LokiStringResponseDTO::data).orElse(List.of());
    }
}
