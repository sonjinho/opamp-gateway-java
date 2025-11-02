package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(value = "agent_health")
public class AgentHealthEntity {
    @Id
    private Long id;
    @Column(value = "is_root")
    private Boolean isRoot;
    @Column(value = "parent")
    private String parent;
    @Column(value = "target_name")
    private String targetName;
    @Column(value = "agent_id")
    private Long agentId;
    @Column(value = "health")
    private Boolean health;
    @Column(value = "start_time")
    private Long startTime;
    @Column(value = "last_error")
    private String lastError;
    @Column(value = "status")
    private String status;
    @Column(value = "status_time")
    private Long statusTime;
    @Column(value = "create_at")
    private LocalDateTime createAt;
}
