package io.opentelemetry.opamp.client.domain.agent;

import lombok.Getter;

// status UNSET = 0, APPLIED = 1, APPLYING = 2, FAILED = 3
@Getter
public enum RemoteConfigStatuses {
    UNSET(0),
    APPLIED(1),
    APPLYING(2),
    FAILED(3);

    private final int value;

    RemoteConfigStatuses(int value) {
        this.value = value;
    }
}
