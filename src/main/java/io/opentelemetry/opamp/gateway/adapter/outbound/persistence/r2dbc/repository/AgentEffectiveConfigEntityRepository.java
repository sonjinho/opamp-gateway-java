package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity.AgentEffectiveConfigEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentEffectiveConfigEntityRepository extends ReactiveCrudRepository<AgentEffectiveConfigEntity, Long> {
}