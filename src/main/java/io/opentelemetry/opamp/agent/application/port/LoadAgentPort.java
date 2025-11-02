package io.opentelemetry.opamp.agent.application.port;

import io.opentelemetry.opamp.agent.domain.AgentDomain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoadAgentPort {
    Mono<AgentDomain> loadAgent(UUID uuid);

    Mono<Boolean> isExist(UUID uuid);

    Flux<AgentDomain> loadActiveAgents();
}
