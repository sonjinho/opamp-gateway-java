package io.opentelemetry.opamp.gateway.application.service;

import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.gateway.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.gateway.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.gateway.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class OpampService implements OpampUseCase {

    private final AgentUseCase agentUseCase;
    private final LoadAgentToServerPort loadAgentToServerPort;
    private final UpdateAgentToServerPort updateAgentToServerPort;

    @Override
    public Mono<ServerToAgentDomain> processRequest(AgentToServerDomain request) {
        return Mono.zip(
                loadAgentToServerPort.loadAgentToServer(request.instanceId()),
                updateAgentToServerPort.saveAgentToServer(request)
        ).flatMap(tuple -> {
            AgentToServerDomain recent = tuple.getT1();
            if (recent == null || !recent.equals(request)) {
                return agentUseCase.saveAgent(request)
                        .then(agentUseCase.loadAgent(request.instanceId()))
                        .map(agent -> ServerToAgentDomain.builder()
                                .instanceId(request.instanceId())
                                .capabilities(request.capabilities())
                                .flags(0L)
                                .build());
            }
            return Mono.just(ServerToAgentDomain.builder()
                    .instanceId(request.instanceId())
                    .capabilities(request.capabilities())
                    .flags(0L)
                    .build());
        });
    }
}
