package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_health")
public class AgentHealthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_root")
    private Boolean isRoot;
    @Column(name = "parent")
    private String parent;
    @Column(name = "target_name")
    private String targetName;
    @Column(name = "agent_id")
    private Long agentId;
    @Column(name = "health")
    private Boolean health;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "last_error")
    private String lastError;
    @Column(name = "status")
    private String status;
    @Column(name = "status_time")
    private Long statusTime;
    @Column(name = "create_at")
    private LocalDateTime createAt;
}
