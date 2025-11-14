package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.configuration;

import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.LokiStringResponseDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryDTO;
import io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto.RequestLokiQueryRangeDTO;

import java.util.Optional;

public interface LokiRestClient {
    Optional<LokiResponseDTO> query(RequestLokiQueryDTO requestQuery);

    Optional<LokiResponseDTO> queryRange(RequestLokiQueryRangeDTO requestQueryRange);

    LokiStringResponseDTO queryLabels();

    LokiStringResponseDTO queryLabelValues(String label);
}
