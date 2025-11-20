package io.opentelemetry.opamp.client.adapter.outbound.persistence.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.opamp.client.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.client.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@ConditionalOnProperty(name = "request.persistence.type", havingValue = "FILE")
@Component
@RequiredArgsConstructor
public class AgentToServerFilePersistenceAdapter implements LoadAgentToServerPort, UpdateAgentToServerPort {
    private final FileStorageService service;
    private final ObjectMapper mapper;

    @Override
    public AgentToServerDomain loadAgentToServer(UUID uuid) {
        return service.read(uuid);
    }

    @Override
    public boolean isExist(UUID uuid) {
        AgentToServerDomain domain = service.read(uuid);
        return domain != null;
    }

    @Override
    public boolean saveAgentToServer(AgentToServerDomain agentToServer) {
        service.save(agentToServer);
        return true;
    }
}
