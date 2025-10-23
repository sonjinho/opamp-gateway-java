package io.opentelemetry.opamp.gateway.mapper;

import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import opamp.proto.Opamp;

public class ServerToAgentMapper {

    public Opamp.ServerToAgent mapperToProto(ServerToAgentDomain serverToAgentDomain) {
        return Opamp.ServerToAgent.newBuilder().build();
    }
}
