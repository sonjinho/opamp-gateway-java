package io.opentelemetry.opamp.config;

import io.opentelemetry.opamp.gateway.adapter.inbound.web.OpampWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket // STOMP가 아닌 일반 WebSocket 기능을 활성화합니다.
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final OpampWebSocketHandler opampWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // OpampWebSocketHandler를 '/opamp-websocket' URL에 등록합니다.
        registry.addHandler(opampWebSocketHandler, "/v1/opamp-ws")
                .setAllowedOrigins("*");
    }
}