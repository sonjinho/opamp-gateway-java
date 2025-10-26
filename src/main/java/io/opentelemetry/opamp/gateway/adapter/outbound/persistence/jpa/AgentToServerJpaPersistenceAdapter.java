package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity.AgentToServerEntity;
import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository.AgentToServerEntityRepository;
import io.opentelemetry.opamp.gateway.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.gateway.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ConditionalOnProperty(name = "request.persistence.type", havingValue = "DB")
@Component
@RequiredArgsConstructor
public class AgentToServerJpaPersistenceAdapter implements LoadAgentToServerPort, UpdateAgentToServerPort {

    private final AgentToServerEntityRepository repo;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public AgentToServerDomain loadAgentToServer(UUID uuid) {
        var entity = repo.findByInstanceUId(uuid).orElseThrow(EntityNotFoundException::new);
        return entity.toDomain();
    }

    @Cacheable(value = "agents", key = "#uuid.toString()")
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public boolean isExist(UUID uuid) {
        return false;
    }

    @Override
    public boolean saveAgentToServer(AgentToServerDomain agentToServer) {
        var entity = AgentToServerEntity.fromDomain(agentToServer);
        repo.save(entity);
        return false;
    }
}
