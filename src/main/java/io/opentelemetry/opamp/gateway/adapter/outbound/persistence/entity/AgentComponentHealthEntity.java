package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentComponentHealthDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_component_health")
public class AgentComponentHealthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column
    private String name;
    @Column
    private Boolean healthy;
    @Column
    private Long startTimeUnixNano;
    private String lastError;
    private String status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private AgentComponentHealthEntity parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<AgentComponentHealthEntity> children;

    public AgentComponentHealthDomain toDomain() {
        return new AgentComponentHealthDomain(
                this.healthy,
                this.startTimeUnixNano,
                this.lastError,
                this.status,
                this.startTimeUnixNano,
                this.children.stream()
                        .collect(Collectors.toMap(AgentComponentHealthEntity::getName, AgentComponentHealthEntity::toDomain))
        );
    }
}
