package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity.AgentComponentHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentComponentHealthEntityRepository extends JpaRepository<AgentComponentHealthEntity, Long> {
}