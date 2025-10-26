package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.gateway.domain.agent.EffectiveConfigDomain;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
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
    @Setter
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

    public void merge(AgentEffectiveConfigEntity target) {
        if (target.agentConfigFiles == null) {
            return;
        }

        if (this.agentConfigFiles == null) {
            this.agentConfigFiles = new HashSet<>();
        }

        // 기존 파일들을 contentType 기준으로 Map 으로 변환
        Map<String, AgentConfigFileEntity> existingMap = this.agentConfigFiles.stream()
                .collect(Collectors.toMap(AgentConfigFileEntity::getName, f -> f));

        // 새로 들어온 파일들을 순회하면서 업데이트 or 추가
        for (AgentConfigFileEntity newFile : target.agentConfigFiles) {
            AgentConfigFileEntity existing = existingMap.get(newFile.getName());
            if (existing != null) {
                // 이미 존재하면 body만 갱신
                existing.setBody(newFile.getBody());
            } else {
                // 새로운 파일이면 관계 설정 후 추가
                newFile.setEffectiveConfig(this);
                this.agentConfigFiles.add(newFile);
            }
        }
    }
}
