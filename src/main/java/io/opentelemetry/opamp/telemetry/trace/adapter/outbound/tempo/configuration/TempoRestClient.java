package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.configuration;

import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.TempoTagNamesQuery;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.TempoTagValuesQuery;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchQueryDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchResponseDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchTagValueResponseDTO;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto.TempoSearchTageNameResponseDTO;
import io.opentelemetry.proto.trace.v1.TracesData;

import java.util.Optional;

public interface TempoRestClient {
    Optional<TracesData> getTrace(String traceId);

    TempoSearchResponseDTO searchTraces(TempoSearchQueryDTO query);

    TempoSearchTageNameResponseDTO getTagNames(TempoTagNamesQuery query);

    TempoSearchTagValueResponseDTO getTagValues(String tagName, TempoTagValuesQuery query);

}
