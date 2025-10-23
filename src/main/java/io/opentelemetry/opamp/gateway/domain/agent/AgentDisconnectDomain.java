package io.opentelemetry.opamp.gateway.domain.agent;

import lombok.Getter;
import lombok.ToString;
import opamp.proto.Opamp;

@ToString
@Getter
public class AgentDisconnectDomain {

    public static AgentDisconnectDomain of(Opamp.AgentDisconnect agentDisconnect) {
        if (agentDisconnect == null) return null;
        return new AgentDisconnectDomain();
    }
}
