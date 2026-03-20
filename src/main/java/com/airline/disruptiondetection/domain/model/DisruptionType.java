package com.airline.disruptiondetection.domain.model;

public enum DisruptionType {
    DELAY,
    CANCELLATION,
    DIVERSION,
    SEVERE_WEATHER,
    TECHNICAL_FAILURE,
    CREW_UNAVAILABILITY,
    AIR_TRAFFIC_CONTROL,
    AIRPORT_CLOSURE;

    public boolean isCritical() {
        return this == CANCELLATION || this == DIVERSION
            || this == SEVERE_WEATHER || this == AIRPORT_CLOSURE;
    }
}
