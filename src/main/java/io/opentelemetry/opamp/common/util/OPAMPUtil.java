package io.opentelemetry.opamp.common.util;

import com.google.protobuf.MessageLite;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentFlags;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public enum OPAMPUtil {
    INSTANCE;

    /**
     * OpAMP 메시지 디코딩 (client → server)
     * WebSocket binary payload = [varint header (usually 0x00)] + [protobuf bytes]
     */
    public byte[] decodeOpampWS(ByteBuffer buffer) {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        int i = 0;
        long header = 0;
        int shift = 0;

        // Base128 Varint decode
        while (i < data.length) {
            byte b = data[i++];
            header |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) break; // MSB=0이면 종료
            shift += 7;
        }

        if (header != 0) {
            log.warn("Unexpected OpAMP header value: {}", header);
        }

        return Arrays.copyOfRange(data, i, data.length);
    }

    /**
     * OpAMP 메시지 인코딩 (server → client)
     * WebSocket binary payload = [varint header (0x00)] + [protobuf bytes]
     */
    public byte[] encodeOpampWS(MessageLite message) {
        byte[] protoBytes = message.toByteArray();
        byte[] header = new byte[]{0x00}; // Varint encoded 0
        byte[] result = new byte[header.length + protoBytes.length];

        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(protoBytes, 0, result, header.length, protoBytes.length);

        return result;
    }

    public ServerToAgentDomain createPong(UUID instanceId) {
        return ServerToAgentDomain.builder().instanceId(instanceId).flags(ServerToAgentFlags.UNSPECIFIED.val()).build();
    }

    public ServerToAgentDomain createInitResponse(UUID instanceId, Long capabilities) {
        return ServerToAgentDomain.builder()
                .instanceId(instanceId)
                .flags(ServerToAgentFlags.REPORT_FULL_STATE.val() & ServerToAgentFlags.REPORT_AVAILABLE_COMPONENTS.val())
                .capabilities(capabilities)
                .build();
    }
}
