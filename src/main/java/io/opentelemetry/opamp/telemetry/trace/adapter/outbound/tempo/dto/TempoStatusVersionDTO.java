package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TempoStatusVersionDTO {
    @JsonProperty("version")
    String version;
    @JsonProperty("revision")
    String revision;
    @JsonProperty("branch")
    String branch;
    @JsonProperty("buildUser")
    String buildUser;
    @JsonProperty("buildDate")
    String buildDate;
    @JsonProperty("goVersion")
    String goVersion;
}
