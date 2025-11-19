package io.opentelemetry.opamp.gateway.adapter.outbound.websocket;

import io.opentelemetry.opamp.common.util.OPAMPUtil;
import io.opentelemetry.opamp.gateway.adapter.inbound.web.WebSocketSessionRegistry;
import io.opentelemetry.opamp.gateway.application.port.AgentPushPort;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.gateway.mapper.ServerToAgentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAgentPushAdapter implements AgentPushPort {

    private final WebSocketSessionRegistry registry;
    private final ServerToAgentMapper mapper;

    @Override
    public void push(UUID agentId, ServerToAgentDomain serverToAgentDomain) {
        Optional<WebSocketSession> session = registry.getSession(agentId);
        if (session.isPresent()) {
            byte[] responseBytes = OPAMPUtil.INSTANCE.encodeOpampWS(mapper.mapperToProto(serverToAgentDomain));
            try {
                session.get().sendMessage(new BinaryMessage(responseBytes));
            } catch (IOException e) {
                log.error("Failed to send OpAMP message to agent {}", agentId, e);
            }
        }
    }
}
