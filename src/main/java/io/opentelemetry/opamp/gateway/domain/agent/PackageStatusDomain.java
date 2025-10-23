package io.opentelemetry.opamp.gateway.domain.agent;

/**
 * @param status INSTALLED 0, INSTALLING 1, INSTALL_FAILED 2
 */
public record PackageStatusDomain(String name, String agentHasVersion, byte[] agentHasHash, String serverOfferedVersion,
                           byte[] serverOfferedHash, Integer status, String errorMessage) {


}
