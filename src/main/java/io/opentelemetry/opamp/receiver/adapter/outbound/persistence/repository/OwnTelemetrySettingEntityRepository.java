package io.opentelemetry.opamp.receiver.adapter.outbound.persistence.repository;

import io.opentelemetry.opamp.receiver.adapter.outbound.persistence.entity.OwnTelemetrySettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OwnTelemetrySettingEntityRepository extends JpaRepository<OwnTelemetrySettingEntity, UUID> {
}