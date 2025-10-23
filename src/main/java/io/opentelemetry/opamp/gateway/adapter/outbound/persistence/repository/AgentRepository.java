package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity.AgentToServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<AgentToServerEntity, Long> {
}
