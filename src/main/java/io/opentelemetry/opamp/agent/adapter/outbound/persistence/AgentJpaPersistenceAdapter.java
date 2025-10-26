package io.opentelemetry.opamp.agent.adapter.outbound.persistence;

import io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity.AgentEntity;
import io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository.AgentEntityRepository;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@ToString
@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class AgentJpaPersistenceAdapter implements UpdateAgentPort {

    private final AgentEntityRepository repo;

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
}


