package io.opentelemetry.opamp.client.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.client.domain.agent.AgentDescriptionDomain;
import io.opentelemetry.opamp.common.StringStringMapConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_description")
public class AgentDescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Convert(converter = StringStringMapConverter.class)
    @Column(length = 10000, columnDefinition = "json")
    private Map<String, String> identifyingAttributes;

    @Convert(converter = StringStringMapConverter.class)
    @Column(length = 10000, columnDefinition = "json")
    private Map<String, String> nonIdentifyingAttributes;

    public AgentDescriptionDomain toDomain() {
        return new AgentDescriptionDomain(this.identifyingAttributes, this.nonIdentifyingAttributes);
    }

    public void merge(AgentDescriptionEntity target) {
        if (target.identifyingAttributes != null) {
            if (this.identifyingAttributes == null) {
                this.identifyingAttributes = target.identifyingAttributes;
            } else {
                this.identifyingAttributes.putAll(target.identifyingAttributes);
            }
        }

        if (target.nonIdentifyingAttributes != null) {
            if (this.nonIdentifyingAttributes == null) {
                this.nonIdentifyingAttributes = target.nonIdentifyingAttributes;
            } else {
                this.nonIdentifyingAttributes.putAll(target.nonIdentifyingAttributes);
            }
        }
    }
}
