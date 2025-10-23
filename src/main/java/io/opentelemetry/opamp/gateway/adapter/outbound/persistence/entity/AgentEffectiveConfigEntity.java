package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.EffectiveConfigDomain;
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
@Table(name = "agent_effective_config")
public class AgentEffectiveConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @OneToMany(mappedBy = "effectiveConfig", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<AgentConfigFileEntity> agentConfigFiles;

    public EffectiveConfigDomain toDomain() {
        return new EffectiveConfigDomain(
                agentConfigFiles.stream()
                        .collect(
                                Collectors.toMap(
                                        AgentConfigFileEntity::getName,
                                        AgentConfigFileEntity::toDomain
                                )
                        )
        );
    }
}
