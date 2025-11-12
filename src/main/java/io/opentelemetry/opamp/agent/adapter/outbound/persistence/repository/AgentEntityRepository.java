package io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository;

import io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentEntityRepository extends JpaRepository<AgentEntity, Long> {
    Optional<AgentEntity> findByInstanceUId(UUID instanceUId);


}