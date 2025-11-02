package io.opentelemetry.opamp.gateway.application.port;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import reactor.core.publisher.Mono;

public interface UpdateAgentToServerPort {
    Mono<Boolean> saveAgentToServer(AgentToServerDomain agentToServer);
}
