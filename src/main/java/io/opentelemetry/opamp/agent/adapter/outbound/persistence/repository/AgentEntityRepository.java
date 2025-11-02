package io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository;

import io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity.AgentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AgentEntityRepository extends R2dbcRepository<AgentEntity, Long> {
    Mono<AgentEntity> findByInstanceUId(UUID instanceUId);
}