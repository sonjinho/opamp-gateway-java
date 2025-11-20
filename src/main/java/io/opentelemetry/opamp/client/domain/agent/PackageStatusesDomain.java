package io.opentelemetry.opamp.client.domain.agent;

import java.util.HashMap;
import java.util.Map;

public record PackageStatusesDomain(
        Map<String, PackageStatusDomain> packages,
        byte[] serverProvidedAllPackagesHash,
        String errorMessage
) {

    public static PackageStatusesDomain merge(PackageStatusesDomain src, PackageStatusesDomain target) {
        if (src == null) {
            return target;
        }
        if (target == null) {
            return src;
        }

        Map<String, PackageStatusDomain> mergedPackages = new HashMap<>(src.packages());
        if (target.packages() != null) {
            target.packages().forEach((key, value) ->
                    mergedPackages.merge(key, value, PackageStatusDomain::merge)
            );
        }

        byte[] mergedServerProvidedAllPackagesHash = src.serverProvidedAllPackagesHash();
        if (target.serverProvidedAllPackagesHash() != null) {
            mergedServerProvidedAllPackagesHash = target.serverProvidedAllPackagesHash();
        }

        String mergedErrorMessage = src.errorMessage();
        if (target.errorMessage() != null) {
            mergedErrorMessage = target.errorMessage();
        }

        return new PackageStatusesDomain(mergedPackages, mergedServerProvidedAllPackagesHash, mergedErrorMessage);

    }
}

