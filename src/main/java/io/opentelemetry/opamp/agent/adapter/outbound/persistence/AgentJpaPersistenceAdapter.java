package io.opentelemetry.opamp.agent.adapter.outbound.persistence;

import io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity.AgentEntity;
import io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository.AgentEntityRepository;
import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@ToString
@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class AgentJpaPersistenceAdapter implements LoadAgentPort, UpdateAgentPort {

    private final AgentEntityRepository repo;

    @Override
    public List<AgentDomain> loadActiveAgents() {
        return repo.findAll().stream().map(AgentEntity::toDomain).toList();
    }

    @Override
    public boolean saveAgent(AgentDomain agent) {
        var entity = repo.findByInstanceUId(agent.instanceUId());
        if (entity.isEmpty()) {
            // save
            repo.save(AgentEntity.from(agent));
        } else {
            // update
            var existAgent = entity.get();
            var current = AgentEntity.from(agent);
            existAgent.merge(current);
        }
        return true;
    }

    @Override
    public AgentDomain loadAgent(UUID uuid) {
        var entity = repo.findByInstanceUId(uuid);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException(
                    "Agent with instance ID " + uuid + " not found"
            );
        }
        return entity.get().toDomain();
    }

    @Override
    public boolean isExist(UUID uuid) {
        var entity = repo.findByInstanceUId(uuid);
        return entity.isPresent();
    }
}


