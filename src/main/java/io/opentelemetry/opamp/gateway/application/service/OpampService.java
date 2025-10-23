package io.opentelemetry.opamp.gateway.application.service;

import io.opentelemetry.opamp.common.util.UUIDUtil;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.mapper.AgentToServerMapper;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opamp.proto.Opamp;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OpampService {

    private final AgentToServerMapper mapper;

    public Opamp.ServerToAgent processRequest(Opamp.AgentToServer request) {
        AgentToServerDomain agentToServer = mapper.mapperToDomain(request);
        log.info("Agent To Server: {}", agentToServer);
        return Opamp.ServerToAgent.newBuilder()
                .setInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(agentToServer.instanceId())))
                .setAgentIdentification(
                        Opamp.AgentIdentification.newBuilder()
                                .setNewInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(UUIDUtil.INSTANCE.generateUUIDv7())))
                                .build()
                )
                .setCapabilities(agentToServer.capabilities())
                .build();
    }


}
