package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentComponentHealthDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(name = "agent_component_health")
public class AgentComponentHealthEntity {
    @Id

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


    public AgentComponentHealthDomain toDomain() {
        return new AgentComponentHealthDomain(
                this.healthy,
                this.startTimeUnixNano,
                this.lastError,
                this.status,
                this.startTimeUnixNano,
                null
        );
    }

    public void merge(AgentComponentHealthEntity target) {
        if (target.healthy != null) {
            this.healthy = target.healthy;
        }
        if (target.startTimeUnixNano != null) {
            this.startTimeUnixNano = target.startTimeUnixNano;
        }
        if (target.lastError != null) {
            this.lastError = target.lastError;
        }
        if (target.status != null) {
            this.status = target.status;
        }
    }
}
