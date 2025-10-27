package io.opentelemetry.opamp.gateway.mapper;

import com.google.protobuf.ByteString;
import io.opentelemetry.opamp.common.util.UUIDUtil;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;

@Component
public class ServerToAgentMapper {

    public Opamp.ServerToAgent mapperToProto(ServerToAgentDomain serverToAgentDomain) {
        return Opamp.ServerToAgent.newBuilder()
                .setInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(serverToAgentDomain.instanceId())))
                .setCapabilities(
                        AgentCapabilitiesHandler.addCapability(AgentCapabilitiesHandler.REPORTS_HEALTH, AgentCapabilitiesHandler.REPORTS_HEARTBEAT)
                )
                .build();
    }
}
