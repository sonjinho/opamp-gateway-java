package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity.AgentToServerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AgentToServerEntityRepository extends ReactiveCrudRepository<AgentToServerEntity, Long> {
    Mono<AgentToServerEntity> findByInstanceUId(UUID uuid);
}
