package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentDisconnectDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_to_server")
public class AgentToServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "instance_uid")
    private UUID instanceUId;
    @Column(name = "seq_num")
    private Long seqNum;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "description_id")
    private AgentDescriptionEntity description;
    @Column(name = "capabilities")
    private Long capabilities;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "component_health_id")
    private AgentComponentHealthEntity componentHealth;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "effective_config_id")
    private AgentEffectiveConfigEntity effectiveConfig;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "remote_config_status_id")
    private AgentRemoteConfigStatusEntity remoteConfigStatus;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "package_statuses_id")
    private AgentPackageStatusesEntity packageStatuses;
    @Column
    private Long flags;
    @CreationTimestamp()
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "disconnected_at", nullable = true)
    private LocalDateTime disconnectedAt;

    public AgentToServerDomain toDomain() {
        return new AgentToServerDomain(
                instanceUId,
                seqNum,
                description.toDomain(),
                capabilities,
                componentHealth.toDomain(),
                effectiveConfig.toDomain(),
                remoteConfigStatus.toDomain(),
                packageStatuses.toDomain(),
                new AgentDisconnectDomain(),
                flags,
                createdAt,
                disconnectedAt
        );
    }


}
