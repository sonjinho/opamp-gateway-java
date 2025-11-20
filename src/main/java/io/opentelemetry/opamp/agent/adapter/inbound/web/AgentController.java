package io.opentelemetry.opamp.agent.adapter.inbound.web;

import io.opentelemetry.opamp.agent.adapter.inbound.web.dto.AgentRemoteConfigDTO;
import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.application.command.ChangeAgentIdCommand;
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

    // GET agent by targetId
    @GetMapping("/{targetId}")
    public AgentDomain getAgent(@PathVariable("targetId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{targetId}")
    public AgentDomain updateAgent(@PathVariable("targetId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{targetId}/config")
    public AgentDomain updateRemoteConfig(@PathVariable("targetId") UUID instanceId, @RequestBody AgentRemoteConfigDTO remoteConfigDTO) {
        agentUseCase.updateRemoteConfig(new UpdateAgentConfigCommand(instanceId, remoteConfigDTO.toDomain()));
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{targetId}/change-id")
    public AgentDomain changeAgentId(@PathVariable("targetId") UUID instanceId, @RequestBody ChangeAgentIdCommand changeAgentIdCommand) {
        boolean result = agentUseCase.changeAgentId(changeAgentIdCommand);
        if (result) {
            return agentUseCase.loadAgent(instanceId);
        } else {
            throw new UnsupportedOperationException("New Agent ID already exists");
        }
    }

}
