package io.opentelemetry.opamp.gateway.domain.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentCapabilitiesHandler {
    // AgentCapabilities enum 값 (Protobuf에서 생성된 값 사용)
    public static final long UNSPECIFIED_AGENT_CAPABILITY = 0L;
    public static final long REPORTS_STATUS = 0x00000001L;
    public static final long ACCEPTS_REMOTE_CONFIG = 0x00000002L;
    public static final long REPORTS_EFFECTIVE_CONFIG = 0x00000004L;
    public static final long ACCEPTS_PACKAGES = 0x00000008L;
    public static final long REPORTS_PACKAGE_STATUSES = 0x00000010L;
    public static final long REPORTS_OWN_TRACES = 0x00000020L;
    public static final long REPORTS_OWN_METRICS = 0x00000040L;
    public static final long REPORTS_OWN_LOGS = 0x00000080L;
    public static final long ACCEPTS_OPAMP_CONNECTION_SETTINGS = 0x00000100L;
    public static final long ACCEPTS_OTHER_CONNECTION_SETTINGS = 0x00000200L;
    public static final long ACCEPTS_RESTART_COMMAND = 0x00000400L;
    public static final long REPORTS_HEALTH = 0x00000800L;
    public static final long REPORTS_REMOTE_CONFIG = 0x00001000L;
    public static final long REPORTS_HEARTBEAT = 0x00002000L;

    // 특정 capability가 설정되어 있는지 확인
    public static boolean hasCapability(long capabilities, long capability) {
        return (capabilities & capability) != 0;
    }

    // capability 설정
    public static long addCapability(long capabilities, long capability) {
        return capabilities | capability;
    }

    // capability 제거
    public static long removeCapability(long capabilities, long capability) {
        return capabilities & ~capability;
    }

    // capability 토글
    public static long toggleCapability(long capabilities, long capability) {
        return capabilities ^ capability;
    }

    // 설정된 모든 capabilities 반환
    public static List<String> getEnabledCapabilities(long capabilities) {
        List<String> enabled = new ArrayList<>();
        if (hasCapability(capabilities, REPORTS_STATUS)) enabled.add("ReportsStatus");
        if (hasCapability(capabilities, ACCEPTS_REMOTE_CONFIG)) enabled.add("AcceptsRemoteConfig");
        if (hasCapability(capabilities, REPORTS_EFFECTIVE_CONFIG)) enabled.add("ReportsEffectiveConfig");
        if (hasCapability(capabilities, ACCEPTS_PACKAGES)) enabled.add("AcceptsPackages");
        if (hasCapability(capabilities, REPORTS_PACKAGE_STATUSES)) enabled.add("ReportsPackageStatuses");
        if (hasCapability(capabilities, REPORTS_OWN_TRACES)) enabled.add("ReportsOwnTraces");
        if (hasCapability(capabilities, REPORTS_OWN_METRICS)) enabled.add("ReportsOwnMetrics");
        if (hasCapability(capabilities, REPORTS_OWN_LOGS)) enabled.add("ReportsOwnLogs");
        if (hasCapability(capabilities, ACCEPTS_OPAMP_CONNECTION_SETTINGS)) enabled.add("AcceptsOpAMPConnectionSettings");
        if (hasCapability(capabilities, ACCEPTS_OTHER_CONNECTION_SETTINGS)) enabled.add("AcceptsOtherConnectionSettings");
        if (hasCapability(capabilities, ACCEPTS_RESTART_COMMAND)) enabled.add("AcceptsRestartCommand");
        if (hasCapability(capabilities, REPORTS_HEALTH)) enabled.add("ReportsHealth");
        if (hasCapability(capabilities, REPORTS_REMOTE_CONFIG)) enabled.add("ReportsRemoteConfig");
        if (hasCapability(capabilities, REPORTS_HEARTBEAT)) enabled.add("ReportsHeartbeat");
        return enabled;
    }

}