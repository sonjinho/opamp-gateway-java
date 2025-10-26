package io.opentelemetry.opamp.gateway.domain.agent;

/**
 * @param status INSTALLED 0, INSTALLING 1, INSTALL_FAILED 2
 */
public record PackageStatusDomain(String name, String agentHasVersion, byte[] agentHasHash, String serverOfferedVersion,
                           byte[] serverOfferedHash, Integer status, String errorMessage) {


    public PackageStatusDomain merge(PackageStatusDomain packageStatusDomain) {
        if (packageStatusDomain == null) {
            return this;
        }

        String mergedName = name();
        String mergedAgentHasVersion = agentHasVersion();
        byte[] mergedAgentHasHash = agentHasHash();
        String mergedServerOfferedVersion = serverOfferedVersion();
        byte[] mergedServerOfferedHash = serverOfferedHash();
        Integer mergedStatus = status();
        String mergedErrorMessage = errorMessage();

        if (packageStatusDomain.name() != null) {
            mergedName = packageStatusDomain.name();
        }
        if (packageStatusDomain.agentHasVersion() != null) {
            mergedAgentHasVersion = packageStatusDomain.agentHasVersion();
        }
        if (packageStatusDomain.agentHasHash() != null) {
            mergedAgentHasHash = packageStatusDomain.agentHasHash();
        }
        if (packageStatusDomain.serverOfferedVersion() != null) {
            mergedServerOfferedVersion = packageStatusDomain.serverOfferedVersion();
        }
        if (packageStatusDomain.serverOfferedHash() != null) {
            mergedServerOfferedHash = packageStatusDomain.serverOfferedHash();
        }
        if (packageStatusDomain.status() != null) {
            mergedStatus = packageStatusDomain.status();
        }
        if (packageStatusDomain.errorMessage() != null) {
            mergedErrorMessage = packageStatusDomain.errorMessage();
        }

        return new PackageStatusDomain(
                mergedName,
                mergedAgentHasVersion,
                mergedAgentHasHash,
                mergedServerOfferedVersion,
                mergedServerOfferedHash,
                mergedStatus,
                mergedErrorMessage
        );

    }
}
