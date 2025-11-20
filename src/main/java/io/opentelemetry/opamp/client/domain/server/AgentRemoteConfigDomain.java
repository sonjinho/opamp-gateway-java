package io.opentelemetry.opamp.client.domain.server;

public record AgentRemoteConfigDomain(
        AgentConfigMapDomain config,
        byte[] configHash
) {
}
