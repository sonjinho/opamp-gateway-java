package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity.AgentHealthEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentHealthEntityRepository extends ReactiveCrudRepository<AgentHealthEntity, Long> {
}