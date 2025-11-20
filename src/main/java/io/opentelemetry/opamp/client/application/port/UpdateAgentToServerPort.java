package io.opentelemetry.opamp.client.application.port;

import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;

public interface UpdateAgentToServerPort {
    boolean saveAgentToServer(AgentToServerDomain agentToServer);
}
