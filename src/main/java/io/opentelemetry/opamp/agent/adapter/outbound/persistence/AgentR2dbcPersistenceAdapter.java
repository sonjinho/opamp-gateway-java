package io.opentelemetry.opamp.agent.adapter.outbound.persistence;

import io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity.AgentEntity;
import io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository.AgentEntityRepository;
import io.opentelemetry.opamp.agent.application.port.LoadAgentPort;
import io.opentelemetry.opamp.agent.application.port.UpdateAgentPort;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@ToString
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentR2dbcPersistenceAdapter implements LoadAgentPort, UpdateAgentPort {

    private final AgentEntityRepository repo;

    @Override
    public Flux<AgentDomain> loadActiveAgents() {
        return repo.findAll().map(AgentEntity::toDomain);
    }

    @Override
    public Mono<Void> saveAgent(AgentDomain agent) {
        return repo.findByInstanceUId(agent.instanceUId())
                .flatMap(entity -> {
                    // update
                    var current = AgentEntity.from(agent);
                    return repo.save(current);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // save
                    return repo.save(AgentEntity.from(agent));
                })).then();
    }

    @Override
    public Mono<AgentDomain> loadAgent(UUID uuid) {
        return repo.findByInstanceUId(uuid)
                .map(AgentEntity::toDomain);
    }

    @Override
    public Mono<Boolean> isExist(UUID uuid) {
        return repo.findByInstanceUId(uuid)
                .map(entity -> true)
                .defaultIfEmpty(false);
    }
}
