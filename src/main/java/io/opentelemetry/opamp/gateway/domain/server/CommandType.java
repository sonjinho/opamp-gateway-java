package io.opentelemetry.opamp.gateway.domain.server;

import lombok.Getter;

@Getter
public enum CommandType {
    RESTART(0)
    ;

    private final int value;

    CommandType(int value) {
        this.value = value;
    }

}
