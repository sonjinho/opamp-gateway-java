package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Opamp;

/**
 * @param status UNSET = 0, APPLIED = 1, APPLYING = 2, FAILED = 3
 */
public record RemoteConfigStatusDomain(byte[] lastRemoteConfigHash, Integer status, String errorMessage) {
    public static RemoteConfigStatusDomain of(Opamp.RemoteConfigStatus value) {
        if (value == null) return null;
        return new RemoteConfigStatusDomain(
                value.getLastRemoteConfigHash().toByteArray(),
                value.getStatus().getNumber(),
                value.getErrorMessage()
        );
    }
}
