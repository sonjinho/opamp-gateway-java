package io.opentelemetry.opamp.gateway.domain.server;

import lombok.Getter;

// comment type for ServerErrorResponseType
@Getter
public enum ServerErrorResponseType {
    UNKNOWN(0),
    BAD_REQUEST(1),
    UNAVAILABLE(2);

    private final int value;

    ServerErrorResponseType(int value) {
        this.value = value;
    }

}
