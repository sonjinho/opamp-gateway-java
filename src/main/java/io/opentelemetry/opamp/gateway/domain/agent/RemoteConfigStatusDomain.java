package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Opamp;

/**
 * @param status UNSET = 0, APPLIED = 1, APPLYING = 2, FAILED = 3
 */
public record RemoteConfigStatusDomain(byte[] lastRemoteConfigHash, Integer status, String errorMessage) {
}
