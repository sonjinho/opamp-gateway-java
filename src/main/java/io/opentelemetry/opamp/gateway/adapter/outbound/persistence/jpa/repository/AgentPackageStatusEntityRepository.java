package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity.AgentPackageStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentPackageStatusEntityRepository extends JpaRepository<AgentPackageStatusEntity, Long> {
}