package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.EffectiveConfigDomain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;


@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(name = "agent_effective_config")
public class AgentEffectiveConfigEntity {
    @Id
    @Column()
    private Long id;
    @Transient
    private Map<String, AgentConfigFileEntity> configMap;

    public EffectiveConfigDomain toDomain() {
        return new EffectiveConfigDomain(
                null
        );
    }

    public void merge(AgentEffectiveConfigEntity target) {
    }
}
