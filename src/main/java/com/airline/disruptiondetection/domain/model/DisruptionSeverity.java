package com.airline.disruptiondetection.domain.model;

public enum DisruptionSeverity {
    LOW,       // < 30 min
    MEDIUM,    // 30-120 min
    HIGH,      // 120-240 min
    CRITICAL;  // > 240 min o cancelación

    public static DisruptionSeverity fromDelayMinutes(int delayMinutes) {
        if (delayMinutes < 30)  return LOW;
        if (delayMinutes < 120) return MEDIUM;
        if (delayMinutes < 240) return HIGH;
        return CRITICAL;
    }

    public static DisruptionSeverity fromCancellation() {
        return CRITICAL;
    }
}
