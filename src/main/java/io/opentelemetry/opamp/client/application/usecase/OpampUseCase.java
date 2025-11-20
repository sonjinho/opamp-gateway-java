package io.opentelemetry.opamp.client.application.usecase;

import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

public interface OpampUseCase {
    ServerToAgentDomain handleRequest(AgentToServerDomain request);
}
