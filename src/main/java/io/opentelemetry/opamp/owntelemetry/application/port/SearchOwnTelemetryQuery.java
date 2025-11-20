package io.opentelemetry.opamp.owntelemetry.application.port;

import java.util.Map;

public record SearchOwnTelemetryQuery(
        String type,
        Boolean ownMetric,
        Boolean ownTrace,
        Boolean ownLog,
        Map<String, String> labels
) {
}
