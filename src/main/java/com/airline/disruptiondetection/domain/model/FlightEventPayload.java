package com.airline.disruptiondetection.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightEventPayload {

    private String        eventId;
    private String        eventType;
    private String        aggregateId;
    private LocalDateTime occurredAt;
    private String        flightNumber;
    private String        origin;
    private String        destination;
    private String        status;
    private int           delayMinutes;
    private int           passengers;

    public FlightEventPayload() {}

    public UUID getFlightId() {
        try {
            return aggregateId != null ? UUID.fromString(aggregateId) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getEventId()                 { return eventId; }
    public void setEventId(String v)           { this.eventId = v; }
    public String getEventType()               { return eventType; }
    public void setEventType(String v)         { this.eventType = v; }
    public String getAggregateId()             { return aggregateId; }
    public void setAggregateId(String v)       { this.aggregateId = v; }
    public LocalDateTime getOccurredAt()       { return occurredAt; }
    public void setOccurredAt(LocalDateTime v) { this.occurredAt = v; }
    public String getFlightNumber()            { return flightNumber; }
    public void setFlightNumber(String v)      { this.flightNumber = v; }
    public String getOrigin()                  { return origin; }
    public void setOrigin(String v)            { this.origin = v; }
    public String getDestination()             { return destination; }
    public void setDestination(String v)       { this.destination = v; }
    public String getStatus()                  { return status; }
    public void setStatus(String v)            { this.status = v; }
    public int getDelayMinutes()               { return delayMinutes; }
    public void setDelayMinutes(int v)         { this.delayMinutes = v; }
    public int getPassengers()                 { return passengers; }
    public void setPassengers(int v)           { this.passengers = v; }
}
