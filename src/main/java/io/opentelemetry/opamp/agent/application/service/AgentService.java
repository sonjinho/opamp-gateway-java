package io.opentelemetry.opamp.agent.application.service;

import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService implements AgentUseCase {

    private final LoadAgentPort loadAgentPort;
    private final UpdateAgentPort updateAgentPort;

    @Cacheable(value = "agents", key = "#uuid.toString()")
    @Override
    public AgentDomain loadAgent(UUID uuid) {
        return loadAgentPort.loadAgent(uuid);
    }

    @Override
    public boolean saveAgent(AgentToServerDomain agentToServer) {
        var agent = new AgentDomain(
                agentToServer.instanceId(),
                agentToServer.capabilities(),
                agentToServer.description(),
                agentToServer.componentHealth(),
                agentToServer.effectiveConfig(),
                agentToServer.remoteConfigStatus(),
                agentToServer.packagesStatuses(),
                null,
                null,
                agentToServer.disconnectedAt()
        );
        return updateAgentPort.saveAgent(agent);
    }

    @Override
    public void updateAgent(ServerToAgentDomain serverToAgentDomain) {

    }
}
