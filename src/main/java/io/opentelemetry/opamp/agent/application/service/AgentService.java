package io.opentelemetry.opamp.agent.application.service;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService implements AgentUseCase {

    private final LoadAgentPort loadAgentPort;
    private final UpdateAgentPort updateAgentPort;

    @Override
    public Flux<AgentDomain> loadAllAgents(SearchAgentsCommand command) {
        return loadAgentPort.loadActiveAgents();
    }

    @Override
    public Mono<AgentDomain> loadAgent(UUID uuid) {
        return loadAgentPort.loadAgent(uuid);
    }

    @Override
    public Mono<Long> requestFlag(UUID uuid) {
        return loadAgentPort.loadAgent(uuid)
                .map(agent -> {
                    if (agent.createdAt().plusMinutes(10).isBefore(java.time.LocalDateTime.now())) {
                        return (long) (1 & 2);
                    }

                    if (agent.disconnectedAt() != null) {
                        return (long) (1 & 2);
                    }

                    return 0L;
                })
                .defaultIfEmpty((long) (1 & 2));
    }

    @Override
    public Mono<Void> saveAgent(AgentToServerDomain agentToServer) {
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
        return updateAgentPort.saveAgent(agent).then();
    }

    @Override
    public void updateRemoteConfig(UpdateAgentConfigCommand command) {

    }

    @Override
    public void updateAgent(ServerToAgentDomain serverToAgentDomain) {

    }


}
