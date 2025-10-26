package io.opentelemetry.opamp.gateway.domain.agent;

/**
 * @param status UNSET = 0, APPLIED = 1, APPLYING = 2, FAILED = 3
 */
public record RemoteConfigStatusDomain(byte[] lastRemoteConfigHash, Integer status, String errorMessage) {
    public static RemoteConfigStatusDomain merge(RemoteConfigStatusDomain src, RemoteConfigStatusDomain target) {
        if (src == null) {
            return target;
        }
        if (target == null) {
            return src;
        }
        // compare src.lastRemoteConfigHash and target.lastRemoteConfigHash
        if (src.lastRemoteConfigHash != null && target.lastRemoteConfigHash != null &&
                !java.util.Arrays.equals(src.lastRemoteConfigHash, target.lastRemoteConfigHash)) {
            // If hashes are different, it means a new config was applied or attempted.
            // We should prioritize the target's status as it's the most recent.
            return target;
        }

        // If hashes are the same or one is null, we need to decide which status to keep.
        // If the target has a FAILED status, it's important to keep it.
        if (target.status != null && target.status.equals(RemoteConfigStatuses.UNSET.getValue())) {
            return target;
        }
        // If the source has a FAILED status and target is not FAILED, keep the source's FAILED status.
        if (src.status != null && src.status.equals(RemoteConfigStatuses.FAILED.getValue()) &&
                (target.status == null || !target.status.equals(RemoteConfigStatuses.FAILED.getValue()))) {
            return src;
        }

        // If target has a more "active" status (APPLYING) than source, use target.
        if (target.status != null && target.status.equals(RemoteConfigStatuses.APPLYING.getValue()) &&
                (src.status == null || !src.status.equals(RemoteConfigStatuses.APPLIED.getValue()))) {
            return target;
        }
        // If source has a more "active" status (APPLYING) than target, use source.
        if (src.status != null && src.status.equals(RemoteConfigStatuses.APPLYING.getValue()) &&
                (target.status == null || !target.status.equals(
                        RemoteConfigStatuses.APPLYING.getValue()
                ))) {
            return src;
        }

        // If target has an APPLIED status, use target.
        return target;

    }
}
