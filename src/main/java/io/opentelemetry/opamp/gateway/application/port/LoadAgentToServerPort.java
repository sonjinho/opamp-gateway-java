package io.opentelemetry.opamp.gateway.application.port;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoadAgentToServerPort {
    Mono<AgentToServerDomain> loadAgentToServer(UUID uuid);

    Mono<Boolean> isExist(UUID uuid);
}
