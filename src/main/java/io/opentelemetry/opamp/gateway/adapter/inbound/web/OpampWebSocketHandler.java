package io.opentelemetry.opamp.gateway.adapter.inbound.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import io.opentelemetry.opamp.gateway.application.usecase.OpampUseCase;
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

import java.nio.ByteBuffer;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpampWebSocketHandler extends AbstractWebSocketHandler {
    // OpampService는 그대로 사용합니다.
    private final OpampUseCase service;
    private final AgentToServerMapper agentToServerMapper;
    private final ServerToAgentMapper serverToAgentMapper;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        try {
            byte[] payload = decodeOpampWS(message.getPayload());

            Opamp.AgentToServer requestBody = Opamp.AgentToServer.parseFrom(payload);
            log.debug("Received OpAMP message: {}", requestBody);

            ServerToAgentDomain responseBody = service.processRequest(agentToServerMapper.mapperToDomain(requestBody));

            byte[] responseBytes = encodeOpampWS(serverToAgentMapper.mapperToProto(responseBody));

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
        log.info("Connection closed");
    }

    /**
     * OpAMP 메시지 디코딩 (client → server)
     * WebSocket binary payload = [varint header (usually 0x00)] + [protobuf bytes]
     */
    private byte[] decodeOpampWS(ByteBuffer buffer) {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        int i = 0;
        long header = 0;
        int shift = 0;

        // Base128 Varint decode
        while (i < data.length) {
            byte b = data[i++];
            header |= (long)(b & 0x7F) << shift;
            if ((b & 0x80) == 0) break; // MSB=0이면 종료
            shift += 7;
        }

        if (header != 0) {
            log.warn("⚠ Unexpected OpAMP header value: {}", header);
        }

        return Arrays.copyOfRange(data, i, data.length);
    }

    /**
     * OpAMP 메시지 인코딩 (server → client)
     * WebSocket binary payload = [varint header (0x00)] + [protobuf bytes]
     */
    private byte[] encodeOpampWS(MessageLite message) {
        byte[] protoBytes = message.toByteArray();
        byte[] header = new byte[]{0x00}; // Varint encoded 0
        byte[] result = new byte[header.length + protoBytes.length];

        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(protoBytes, 0, result, header.length, protoBytes.length);

        return result;
    }
}
