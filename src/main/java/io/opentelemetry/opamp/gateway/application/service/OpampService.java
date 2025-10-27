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

@Service
@Slf4j
@AllArgsConstructor
public class OpampService implements OpampUseCase {

    private final AgentUseCase agentUseCase;
    private final LoadAgentToServerPort loadAgentToServerPort;
    private final UpdateAgentToServerPort updateAgentToServerPort;

    @Override
    public ServerToAgentDomain processRequest(AgentToServerDomain request) {
        var recent = loadAgentToServerPort.loadAgentToServer(request.instanceId());
        updateAgentToServerPort.saveAgentToServer(request);
        if (recent == null || !recent.equals(request)) {
            // update to recent agent status
            agentUseCase.saveAgent(request);
        }
        return ServerToAgentDomain.builder()
                .instanceId(request.instanceId())
                .capabilities(request.capabilities())
                .build();
    }
}
