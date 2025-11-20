package io.opentelemetry.opamp.client.adapter.outbound.persistence.jpa.repository;

import io.opentelemetry.opamp.client.adapter.outbound.persistence.jpa.entity.AgentConfigFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentConfigFileEntityRepository extends JpaRepository<AgentConfigFileEntity, Long> {
}