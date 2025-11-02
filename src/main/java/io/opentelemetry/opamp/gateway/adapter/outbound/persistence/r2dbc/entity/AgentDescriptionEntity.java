package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentDescriptionDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Getter
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agent_description")
public class AgentDescriptionEntity {
    @Id
    private Long id;

    @Column(value = "identifying_attributes")
    private Map<String, String> identifyingAttributes;

    @Column(value = "non_identifying_attributes")
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
