package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.opentelemetry.opamp.telemetry.metric.domain.OtelMetricRecord;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "resultType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScalarData.class, name = "scalar"),
        @JsonSubTypes.Type(value = StringData.class, name = "string"),
        @JsonSubTypes.Type(value = VectorData.class, name = "vector"),
        @JsonSubTypes.Type(value = MatrixData.class, name = "matrix")
})
public sealed interface PrometheusResultData permits ScalarData, StringData, VectorData, MatrixData {
    List<OtelMetricRecord> toMetricRecord();
}
