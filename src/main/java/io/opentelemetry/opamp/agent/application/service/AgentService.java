package io.opentelemetry.opamp.agent.application.service;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.application.port.AgentPushPort;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static io.opentelemetry.opamp.config.redis.RedisConfig.AGENT_DOMAIN_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService implements AgentUseCase {

    private final LoadAgentPort loadAgentPort;
    private final UpdateAgentPort updateAgentPort;
    private final AgentPushPort agentPushPort;

    @Override
    public List<AgentDomain> loadAllAgents(SearchAgentsCommand command) {
        return loadAgentPort.loadActiveAgents();
    }

    @Cacheable(value = AGENT_DOMAIN_CACHE, key = "#uuid.toString()", sync = true)
    @Override
    public AgentDomain loadAgent(UUID uuid) {
        return loadAgentPort.loadAgent(uuid);
    }

    @Override
    public Long requestFlag(UUID uuid) {
        AgentDomain agent = loadAgentPort.loadAgent(uuid);
        if (agent == null) {
            return (long) (1 & 2);
        }
        // updated Before 10 minute
        // agent.createAt before current - 10min
        if (agent.createdAt().plusMinutes(10).isBefore(java.time.LocalDateTime.now())) {
            return (long) (1 & 2);
        }

        if (agent.disconnectedAt() != null) {
            return (long) (1 & 2);
        }

        return 0L;
    }

    @CacheEvict(value = AGENT_DOMAIN_CACHE, key = "#agentToServer.instanceId().toString()")
    @Override
    public void saveAgent(AgentToServerDomain agentToServer) {
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
        updateAgentPort.saveAgent(agent);
    }

    @Override
    public void updateRemoteConfig(UpdateAgentConfigCommand command) {
        AgentDomain agent = loadAgentPort.loadAgent(command.instanceId());
        if (agent == null) {
            throw new EntityNotFoundException("Agent not found");
        }
        var response = ServerToAgentDomain.builder()
                .flags(0L)
                .instanceId(agent.instanceUId())
                .agentRemoteConfig(command.agentRemoteConfig())
                .build();
        agentPushPort.push(agent.instanceUId(), response);
    }

    @Override
    public void updateAgent(ServerToAgentDomain serverToAgentDomain) {
        agentPushPort.push(serverToAgentDomain.instanceId(), serverToAgentDomain);
    }


}
