package io.opentelemetry.opamp.gateway.application.usecase;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;

import java.util.UUID;

public interface OpampUseCase {
    ServerToAgentDomain processRequest(AgentToServerDomain request);

    void push(UUID agentId, ServerToAgentDomain response);
}
