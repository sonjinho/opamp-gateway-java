package io.opentelemetry.opamp.agent.adapter.inbound.web;

import io.opentelemetry.opamp.agent.adapter.inbound.web.dto.AgentRemoteConfigDTO;
import io.opentelemetry.opamp.agent.adapter.inbound.web.dto.ResponseEventDTO;
import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.usecase.AgentUseCase;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.application.command.ChangeAgentIdCommand;
import io.opentelemetry.opamp.client.application.command.UpdateAgentConfigCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/agents")
@RestController
@Slf4j
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
    public ResponseEventDTO updateRemoteConfig(@PathVariable("targetId") UUID instanceId, @RequestBody AgentRemoteConfigDTO remoteConfigDTO) {
        boolean result = agentUseCase.updateRemoteConfig(new UpdateAgentConfigCommand(instanceId, remoteConfigDTO.toDomain()));
        return new ResponseEventDTO(result, result ? "" : "Failed to update remote config");
    }

    @PutMapping("/{targetId}/change-id")
    public ResponseEventDTO changeAgentId(@PathVariable("targetId") UUID instanceId, @RequestBody ChangeAgentIdCommand changeAgentIdCommand) {
        boolean result = agentUseCase.changeAgentId(changeAgentIdCommand);
        log.info("Target agent Id: {}, event: {}", instanceId, changeAgentIdCommand);
        return new ResponseEventDTO(result, result ? "" : "Failed to change agent id");
    }

    @PutMapping("/{targetId}/restart")
    public ResponseEventDTO restartAgent(@PathVariable("targetId") UUID instanceId) {
        boolean result = agentUseCase.restart(instanceId);
        return new ResponseEventDTO(result, result ? "" : "Failed to restart agent");
    }

    @PutMapping("/{targetId}/own-telemetry/{telemetryId}")
    public ResponseEventDTO ownTelemetryConnection(@PathVariable("targetId") UUID targetId, @PathVariable("telemetryId") UUID telemetryId) {
        boolean result = agentUseCase.changeOwnTelemetry(targetId, telemetryId);
        return new ResponseEventDTO(result, result ? "" : "Failed to change own telemetry");
    }

}
