package io.opentelemetry.opamp.gateway.domain.session;

public record AgentSession(
        String agentId,
        long connectedAt,
        long lastSeen,
        long configVersion
) {
}
