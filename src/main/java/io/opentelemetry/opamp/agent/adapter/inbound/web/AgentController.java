package io.opentelemetry.opamp.agent.adapter.inbound.web;

import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/agents")
@RestController
@RequiredArgsConstructor
public class AgentController {

    private final AgentUseCase agentUseCase;

    // GET search all agents

    // GET agent by instanceId
    @GetMapping("/{instanceId}")
    public AgentDomain getAgent(@PathVariable("instanceId") UUID instanceId) {
        return agentUseCase.loadAgent(instanceId);
    }


}
