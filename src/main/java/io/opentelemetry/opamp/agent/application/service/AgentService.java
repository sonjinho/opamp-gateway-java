package io.opentelemetry.opamp.agent.application.service;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.application.command.ChangeAgentIdCommand;
import io.opentelemetry.opamp.client.application.command.ChangeOwnTelemetryCommand;
import io.opentelemetry.opamp.client.application.command.RestartAgentCommand;
import io.opentelemetry.opamp.client.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.client.application.port.AgentCommandQueuePort;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.owntelemetry.application.port.LoadOwnTelemetrySettingPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.opentelemetry.opamp.config.redis.RedisConfig.AGENT_DOMAIN_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService implements AgentUseCase {

    private final LoadAgentPort loadAgentPort;
    private final UpdateAgentPort updateAgentPort;
    private final AgentCommandQueuePort agentCommandQueuePort;
    private final LoadOwnTelemetrySettingPort loadOwnTelemetrySettingPort;

    @Override
    public List<AgentDomain> loadAllAgents(SearchAgentsCommand command) {
        return loadAgentPort.loadActiveAgents();
    }

    @Override
    public boolean restart(UUID instanceId) {
        return agentCommandQueuePort.enqueue(new RestartAgentCommand(instanceId));
    }

    @Override
    public boolean changeOwnTelemetry(UUID targetId, UUID ownTelemetryId) {
        var isExist = loadAgentPort.isExist(targetId);
        var ownTelemetry = loadOwnTelemetrySettingPort.loadOwnTelemetry(ownTelemetryId);
        if (isExist && Objects.nonNull(ownTelemetry)) {
            return agentCommandQueuePort.enqueue(new ChangeOwnTelemetryCommand(targetId, ownTelemetry));
        }
        return false;
    }

    @Cacheable(value = AGENT_DOMAIN_CACHE, key = "#uuid.toString()", sync = true)
    @Override
    public AgentDomain loadAgent(UUID uuid) {
        return loadAgentPort.loadAgent(uuid);
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
    public boolean updateRemoteConfig(UpdateAgentConfigCommand command) {
        AgentDomain agent = loadAgentPort.loadAgent(command.targetId());
        if (agent == null) {
            throw new EntityNotFoundException("Agent not found");
        }
        return agentCommandQueuePort.enqueue(command);
    }

    @Override
    public boolean changeAgentId(ChangeAgentIdCommand command) {
        if (loadAgentPort.isExist(command.newAgentId()) && loadAgentPort.isExist(command.targetId())) {
            return false;
        }
        return agentCommandQueuePort.enqueue(command);
    }

}
