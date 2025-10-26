package io.opentelemetry.opamp.gateway.application.service;

import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.gateway.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.gateway.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OpampService implements OpampUseCase {

    private final AgentUseCase agentUseCase;
    private final UpdateAgentToServerPort updateAgentToServerPort;

    @Override
    public ServerToAgentDomain processRequest(AgentToServerDomain request) {
        boolean result = updateAgentToServerPort.saveAgentToServer(request);
        if (!result) {
            log.error("Failed To save {}", request.instanceId());
        }
        agentUseCase.saveAgent(request);
        return ServerToAgentDomain.builder()
                .instanceId(request.instanceId())
                .capabilities(request.capabilities())
                .build();
    }
}
