package io.opentelemetry.opamp.gateway.application.port;

import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;

import java.util.UUID;

public interface AgentPushPort {
    void push(UUID agentId, ServerToAgentDomain serverToAgentDomain);
}
