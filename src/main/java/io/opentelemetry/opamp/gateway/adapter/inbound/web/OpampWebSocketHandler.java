package io.opentelemetry.opamp.gateway.adapter.inbound.web;

import com.google.protobuf.InvalidProtocolBufferException;
import io.opentelemetry.opamp.common.util.OPAMPUtil;
import io.opentelemetry.opamp.gateway.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.gateway.mapper.AgentToServerMapper;
import io.opentelemetry.opamp.gateway.mapper.ServerToAgentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpampWebSocketHandler extends AbstractWebSocketHandler {
    // OpampService는 그대로 사용합니다.
    private final OpampUseCase service;
    private final AgentToServerMapper agentToServerMapper;
    private final ServerToAgentMapper serverToAgentMapper;
    private final WebSocketSessionRegistry registry;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        try {
            log.info("handleBinaryMessage: {}", session.getId());
            byte[] payload = OPAMPUtil.INSTANCE.decodeOpampWS(message.getPayload());

            Opamp.AgentToServer requestBody = Opamp.AgentToServer.parseFrom(payload);
            log.debug("Received OpAMP message: {}", requestBody);
            AgentToServerDomain agentToServerDomain = agentToServerMapper.mapperToDomain(requestBody);
            registry.register(agentToServerDomain.instanceId(), session);
            ServerToAgentDomain responseBody = service.processRequest(agentToServerDomain);

            byte[] responseBytes = OPAMPUtil.INSTANCE.encodeOpampWS(serverToAgentMapper.mapperToProto(responseBody));

            session.sendMessage(new BinaryMessage(responseBytes));

        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to parse Protobuf: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in OpAMP handler", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        registry.unregister(session.getId());
        log.info("Connection closed {}", session.getId());
    }


}
