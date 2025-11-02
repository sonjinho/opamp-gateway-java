package io.opentelemetry.opamp.gateway.application.usecase;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import reactor.core.publisher.Mono;

public interface OpampUseCase {
    Mono<ServerToAgentDomain> processRequest(AgentToServerDomain request);
}
