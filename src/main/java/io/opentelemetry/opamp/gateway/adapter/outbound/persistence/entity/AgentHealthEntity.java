package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

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
