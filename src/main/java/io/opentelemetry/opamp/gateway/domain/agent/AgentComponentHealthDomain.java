package io.opentelemetry.opamp.gateway.domain.agent;

import java.util.Map;

public record AgentComponentHealthDomain(
        boolean healthy,
        long startTimeUnixNano,
        String lastError,
        String status,
        long statusTimeUnixNano,
        Map<String, AgentComponentHealthDomain> componentHealthMap
) {
}
