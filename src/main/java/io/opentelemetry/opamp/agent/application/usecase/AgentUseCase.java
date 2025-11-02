package io.opentelemetry.opamp.agent.application.usecase;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AgentUseCase {
    Mono<AgentDomain> loadAgent(UUID uuid);

    Mono<Void> saveAgent(AgentToServerDomain agentToServer);

    default void updateRemoteConfig(UpdateAgentConfigCommand command) {
    }

    void updateAgent(ServerToAgentDomain serverToAgentDomain);

    Mono<Long> requestFlag(UUID uuid);

    Flux<AgentDomain> loadAllAgents(SearchAgentsCommand command);

}
