package io.opentelemetry.opamp.client.application.service;

import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.client.application.port.AgentPushPort;
import io.opentelemetry.opamp.client.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.client.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.client.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
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
    private final AgentPushPort agentPushPort;

    @Override
    public ServerToAgentDomain processRequest(AgentToServerDomain request) {
        AgentToServerDomain recent = loadAgentToServerPort.loadAgentToServer(request.instanceId());
        updateAgentToServerPort.saveAgentToServer(request);
        Long flag = 0L;
        if (recent == null || !recent.equals(request)) {
            agentUseCase.saveAgent(request);
            agentUseCase.loadAgent(request.instanceId());
        }
        return ServerToAgentDomain.builder()
                .instanceId(request.instanceId())
                .capabilities(request.capabilities())
                .flags(flag)
                .build();
    }

    @Override
    public void publishRequest(AgentToServerDomain request) {
        AgentToServerDomain recent = loadAgentToServerPort.loadAgentToServer(request.instanceId());
        updateAgentToServerPort.saveAgentToServer(request);
        Long flag = 0L;
        if (recent == null || !recent.equals(request)) {
            // init agent
            agentUseCase.saveAgent(request);
            agentUseCase.loadAgent(request.instanceId());
            ServerToAgentDomain initResp = ServerToAgentDomain.builder()
                    .instanceId(request.instanceId())
                    .capabilities(request.capabilities())
                    .flags(flag)
                    .build();
            agentPushPort.push(request.instanceId(), initResp);
        }
    }
}
