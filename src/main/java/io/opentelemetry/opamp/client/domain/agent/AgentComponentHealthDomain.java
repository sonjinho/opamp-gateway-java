package io.opentelemetry.opamp.client.domain.agent;

import java.util.Map;

public record AgentComponentHealthDomain(
        Boolean healthy,
        Long startTimeUnixNano,
        String lastError,
        String status,
        Long statusTimeUnixNano,
        Map<String, AgentComponentHealthDomain> componentHealthMap
) {
    public static AgentComponentHealthDomain merge(AgentComponentHealthDomain src, AgentComponentHealthDomain target) {
        if (src == null) {
            return target;
        }
        if (target == null) {
            return src;
        }

        if (target.statusTimeUnixNano != null && target.statusTimeUnixNano == 0) {
            return src;
        }

        // recursive componentHealthMap
        return new AgentComponentHealthDomain(
                target.healthy,
                target.startTimeUnixNano,
                target.lastError,
                target.status,
                target.statusTimeUnixNano,
                target.componentHealthMap.isEmpty() ? src.componentHealthMap : target.componentHealthMap
        );

    }
}
