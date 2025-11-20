package io.opentelemetry.opamp.client.domain.session;

public record AgentSession(
        String agentId,
        long connectedAt,
        long lastSeen,
        long configVersion
) {
}
