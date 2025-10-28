package io.opentelemetry.opamp.agent.adapter.inbound.web;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
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

    // GET agent by instanceId
    @GetMapping("/{instanceId}")
    public AgentDomain getAgent(@PathVariable("instanceId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }

    @PutMapping("/{instanceId}")
    public AgentDomain updateAgent(@PathVariable("instanceId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }


}
