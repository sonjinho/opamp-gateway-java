package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agent_to_server")
public class AgentToServerEntity {
    @Id
    private Long id;
    @Column(value = "instance_uid")
    private UUID instanceUId;
    @Column(value = "seq_num")
    private Long seqNum;
    private AgentDescriptionEntity description;
    @Column(value = "capabilities")
    private Long capabilities;
    private AgentComponentHealthEntity componentHealth;
    private AgentEffectiveConfigEntity effectiveConfig;
    private AgentRemoteConfigStatusEntity remoteConfigStatus;
    private AgentPackageStatusesEntity packageStatuses;
    @Column
    private Long flags;
    @CreatedDate()
    @Column(value = "created_at")
    private LocalDateTime createdAt;
    @Column(value = "disconnected_at")
    private LocalDateTime disconnectedAt;

    public AgentToServerDomain toDomain() {
        return null;
    }
}
