package io.opentelemetry.opamp.agent.adapter.inbound.web;

import io.opentelemetry.opamp.agent.adapter.inbound.web.dto.AgentRemoteConfigDTO;
import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.application.command.UpdateAgentConfigCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/agents")
@RestController
// allowed all
@CrossOrigin("*")
@RequiredArgsConstructor
public class AgentController {

    private final AgentUseCase agentUseCase;

    // GET search all agents

    @GetMapping()
    public List<AgentDomain> listAgent() {
        return agentUseCase.loadAllAgents(SearchAgentsCommand.builder().active(true).build());
    }

    // GET agent by agentId
    @GetMapping("/{agentId}")
    public AgentDomain getAgent(@PathVariable("agentId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{agentId}")
    public AgentDomain updateAgent(@PathVariable("agentId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{agentId}/config")
    public AgentDomain updateRemoteConfig(@PathVariable("agentId") UUID instanceId, @RequestBody AgentRemoteConfigDTO remoteConfigDTO) {
        agentUseCase.updateRemoteConfig(new UpdateAgentConfigCommand(instanceId, remoteConfigDTO.toDomain()));
        return agentUseCase.loadAgent(instanceId);
    }

}
