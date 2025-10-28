package io.opentelemetry.opamp.gateway.mapper;

import com.google.protobuf.ByteString;
import io.opentelemetry.opamp.common.util.UUIDUtil;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.opentelemetry.opamp.gateway.mapper.AgentCapabilitiesHandler.*;

@Component
public class ServerToAgentMapper {

    public Opamp.ServerToAgent mapperToProto(ServerToAgentDomain serverToAgentDomain) {
        return Opamp.ServerToAgent.newBuilder()
                .setInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(serverToAgentDomain.instanceId())))
                .setCapabilities(
                        AgentCapabilitiesHandler.addCapability(List.of(REPORTS_STATUS, ACCEPTS_REMOTE_CONFIG, REPORTS_EFFECTIVE_CONFIG, REPORTS_HEALTH, REPORTS_REMOTE_CONFIG, REPORTS_HEARTBEAT))
                )
                .setFlags(serverToAgentDomain.flags())
                .build();
    }
}
