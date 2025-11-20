package io.opentelemetry.opamp.client.domain.server;

public enum ServerToAgentFlags {
    UNSPECIFIED(0),
    REPORT_FULL_STATE(1),
    REPORT_AVAILABLE_COMPONENTS(2);

    private final long value;

    ServerToAgentFlags(long value) {
        this.value = value;
    }

    public long val() {
        return value;
    }
}
