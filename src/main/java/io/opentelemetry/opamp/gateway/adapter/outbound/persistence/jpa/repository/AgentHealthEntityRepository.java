package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity.AgentHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentHealthEntityRepository extends JpaRepository<AgentHealthEntity, Long> {
}