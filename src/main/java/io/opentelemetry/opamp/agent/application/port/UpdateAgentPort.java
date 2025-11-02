package io.opentelemetry.opamp.agent.application.port;

import io.opentelemetry.opamp.agent.domain.AgentDomain;
import reactor.core.publisher.Mono;

public interface UpdateAgentPort {

    Mono<Boolean> saveAgent(AgentDomain agent);
}
