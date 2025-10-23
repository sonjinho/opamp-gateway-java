package io.opentelemetry.opamp.gateway.domain.server;

public record AgentRemoteConfigDomain(
        AgentConfigMapDomain config,
        byte[] configHash
) {
}
