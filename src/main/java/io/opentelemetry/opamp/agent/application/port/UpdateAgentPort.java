package io.opentelemetry.opamp.agent.application.port;

import io.opentelemetry.opamp.agent.domain.AgentDomain;

public interface UpdateAgentPort {

    boolean saveAgent(AgentDomain agent);
}
