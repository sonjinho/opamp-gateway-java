package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity.AgentToServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentToServerEntityRepository extends JpaRepository<AgentToServerEntity, Long> {
    Optional<AgentToServerEntity> findByInstanceUId(UUID instanceUId);
}
