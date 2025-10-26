package io.opentelemetry.opamp.gateway.application.port;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;

public interface UpdateAgentToServerPort {
    boolean saveAgentToServer(AgentToServerDomain agentToServer);
}
