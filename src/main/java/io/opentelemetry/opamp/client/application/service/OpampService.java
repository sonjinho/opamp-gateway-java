package io.opentelemetry.opamp.client.application.service;

import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.client.application.port.AgentCommandQueuePort;
import io.opentelemetry.opamp.client.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.client.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.client.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.mapper.AgentCapabilitiesHandler;
import io.opentelemetry.opamp.common.util.OPAMPUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class OpampService implements OpampUseCase {

    private final AgentUseCase agentUseCase;
    private final LoadAgentToServerPort loadAgentToServerPort;
    private final UpdateAgentToServerPort updateAgentToServerPort;
    private final AgentCommandQueuePort agentCommandQueuePort;

    // HTTP pull
    @Override
    public ServerToAgentDomain handleRequest(AgentToServerDomain request) {
        AgentToServerDomain recent = loadAgentToServerPort.loadAgentToServer(request.instanceId());
        updateAgentToServerPort.saveAgentToServer(request);
        if (recent == null) {
            agentUseCase.saveAgent(request);

            return OPAMPUtil.INSTANCE.createInitResponse(request.instanceId(), AgentCapabilitiesHandler.enableAllCapabilities());
        } else if (!recent.equals(request)) {
            // The problem is here
            agentUseCase.saveAgent(request);
        }
        Optional<ServerToAgentDomain> resp = agentCommandQueuePort.pollNextCommand(request.instanceId());
        // No Event Then Pong
        return resp.orElseGet(() -> OPAMPUtil.INSTANCE.createPong(request.instanceId()));
    }

}
