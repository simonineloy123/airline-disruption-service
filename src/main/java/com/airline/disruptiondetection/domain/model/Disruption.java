package com.airline.disruptiondetection.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Disruption {

    private UUID               id;
    private UUID               flightId;
    private String             flightNumber;
    private String             origin;
    private String             destination;
    private DisruptionType     type;
    private DisruptionSeverity severity;
    private String             description;
    private int                delayMinutes;
    private int                passengers;
    private LocalDateTime      detectedAt;
    private LocalDateTime      resolvedAt;
    private boolean            resolved;

    private Disruption() {}

    public static Builder builder() {
        return new Builder();
    }

    public void resolve() {
        this.resolved   = true;
        this.resolvedAt = LocalDateTime.now();
    }

    public boolean isCritical() {
        return this.severity == DisruptionSeverity.CRITICAL
            || this.type.isCritical();
    }

    public static class Builder {
        private final Disruption d = new Disruption();

        public Builder id(UUID id)                        { d.id = id; return this; }
        public Builder flightId(UUID flightId)            { d.flightId = flightId; return this; }
        public Builder flightNumber(String fn)            { d.flightNumber = fn; return this; }
        public Builder origin(String origin)              { d.origin = origin; return this; }
        public Builder destination(String destination)    { d.destination = destination; return this; }
        public Builder type(DisruptionType type)          { d.type = type; return this; }
        public Builder severity(DisruptionSeverity s)     { d.severity = s; return this; }
        public Builder description(String description)    { d.description = description; return this; }
        public Builder delayMinutes(int delayMinutes)     { d.delayMinutes = delayMinutes; return this; }
        public Builder passengers(int passengers)         { d.passengers = passengers; return this; }
        public Builder detectedAt(LocalDateTime dt)       { d.detectedAt = dt; return this; }
        public Builder resolvedAt(LocalDateTime dt)       { d.resolvedAt = dt; return this; }
        public Builder resolved(boolean resolved)         { d.resolved = resolved; return this; }

        public Disruption build() {
            if (d.id == null)         d.id = UUID.randomUUID();
            if (d.detectedAt == null) d.detectedAt = LocalDateTime.now();
            if (d.severity == null)   d.severity = DisruptionSeverity.fromDelayMinutes(d.delayMinutes);
            validate();
            return d;
        }

        private void validate() {
            if (d.flightId == null)
                throw new IllegalArgumentException("El ID del vuelo es obligatorio");
            if (d.flightNumber == null || d.flightNumber.isBlank())
                throw new IllegalArgumentException("El número de vuelo es obligatorio");
            if (d.type == null)
                throw new IllegalArgumentException("El tipo de disrupción es obligatorio");
        }
    }

    public UUID getId()                     { return id; }
    public UUID getFlightId()               { return flightId; }
    public String getFlightNumber()         { return flightNumber; }
    public String getOrigin()               { return origin; }
    public String getDestination()          { return destination; }
    public DisruptionType getType()         { return type; }
    public DisruptionSeverity getSeverity() { return severity; }
    public String getDescription()          { return description; }
    public int getDelayMinutes()            { return delayMinutes; }
    public int getPassengers()              { return passengers; }
    public LocalDateTime getDetectedAt()    { return detectedAt; }
    public LocalDateTime getResolvedAt()    { return resolvedAt; }
    public boolean isResolved()             { return resolved; }
}
