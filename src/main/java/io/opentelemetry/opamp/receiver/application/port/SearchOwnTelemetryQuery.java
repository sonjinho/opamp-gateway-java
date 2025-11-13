package io.opentelemetry.opamp.receiver.application.port;

import java.util.Map;

public record SearchOwnTelemetryQuery(
        String type,
        boolean ownMetric,
        boolean ownTrace,
        boolean ownLog,
        Map<String, String> labels
) {
}
