package io.opentelemetry.opamp.agent.application.command;

import io.opentelemetry.opamp.gateway.domain.server.AgentRemoteConfigDomain;

import java.util.UUID;

public record UpdateAgentConfigCommand(
        UUID instanceId,
        AgentRemoteConfigDomain agentRemoteConfig
) {
}
