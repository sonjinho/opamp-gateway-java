package io.opentelemetry.opamp.gateway.application.usecase;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;

public interface OpampUseCase {
    ServerToAgentDomain processRequest(AgentToServerDomain request);
}
