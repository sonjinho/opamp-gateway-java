package io.opentelemetry.opamp.gateway.mapper;

import com.google.protobuf.ByteString;
import io.opentelemetry.opamp.common.util.UUIDUtil;
import io.opentelemetry.opamp.gateway.domain.server.AgentRemoteConfigDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.opentelemetry.opamp.gateway.mapper.AgentCapabilitiesHandler.*;

@Component
public class ServerToAgentMapper {

    public Opamp.ServerToAgent mapperToProto(ServerToAgentDomain serverToAgentDomain) {
        var builder = Opamp.ServerToAgent.newBuilder()
                .setInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(serverToAgentDomain.instanceId())))
                .setCapabilities(
                        AgentCapabilitiesHandler.addCapability(List.of(REPORTS_STATUS, ACCEPTS_REMOTE_CONFIG, REPORTS_EFFECTIVE_CONFIG, REPORTS_HEALTH, REPORTS_REMOTE_CONFIG, REPORTS_HEARTBEAT, REPORTS_OWN_LOGS, REPORTS_OWN_METRICS, REPORTS_OWN_TRACES))
                )
                .setFlags(serverToAgentDomain.flags());
        if (serverToAgentDomain.agentRemoteConfig() != null)
            builder.setRemoteConfig(mapperToProtoAgentRemoteConfig(serverToAgentDomain.agentRemoteConfig()));
        return builder.build();
    }

    public Opamp.AgentRemoteConfig mapperToProtoAgentRemoteConfig(AgentRemoteConfigDomain agentRemoteConfig) {
        Map<String, Opamp.AgentConfigFile> configMap = agentRemoteConfig.config().configMap()
                .entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Opamp.AgentConfigFile
                                        .newBuilder()
                                        .setContentType(entry.getValue().contentType())
                                        .setBody(ByteString.copyFrom(entry.getValue().body()))
                                        .build()
                        )
                );

        return Opamp.AgentRemoteConfig.newBuilder()
                .setConfig(Opamp.AgentConfigMap.newBuilder().putAllConfigMap(configMap).build())
                .setConfigHash(ByteString.copyFrom(agentRemoteConfig.configHash()))
                .build();
    }
}
