package io.opentelemetry.opamp.client.domain.agent;


/*
    // The value of status field is not set.
    ConnectionSettingsStatuses_UNSET = 0;
    // ConnectionSettings were successfully applied by the Agent.
    ConnectionSettingsStatuses_APPLIED = 1;
    // Agent is currently applying the ConnectionSettings that it received.
    ConnectionSettingsStatuses_APPLYING = 2;
    // Agent tried to apply the ConnectionSettings it received earlier, but failed.
    // See error_message for more details.
    ConnectionSettingsStatuses_FAILED = 3;
*/

public record ConnectionSettingsStatusDomain(
        byte[] lastConnectionSettingsHash,
        Integer status,
        String errorMessage
) {
}
