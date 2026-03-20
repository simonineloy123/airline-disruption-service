package com.airline.disruptiondetection.infrastructure.adapter.out.persistence;

import com.airline.disruptiondetection.domain.model.DisruptionSeverity;
import com.airline.disruptiondetection.domain.model.DisruptionType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "disruptions")
public class DisruptionEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    public UUID id;

    @Column(name = "flight_id", nullable = false)
    public UUID flightId;

    @Column(name = "flight_number", nullable = false, length = 10)
    public String flightNumber;

    @Column(name = "origin", length = 10)
    public String origin;

    @Column(name = "destination", length = 10)
    public String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    public DisruptionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 20)
    public DisruptionSeverity severity;

    @Column(name = "description", length = 500)
    public String description;

    @Column(name = "delay_minutes", nullable = false)
    public int delayMinutes;

    @Column(name = "passengers", nullable = false)
    public int passengers;

    @Column(name = "detected_at", nullable = false)
    public LocalDateTime detectedAt;

    @Column(name = "resolved_at")
    public LocalDateTime resolvedAt;

    @Column(name = "resolved", nullable = false)
    public boolean resolved;

    @PrePersist
    public void prePersist() {
        if (detectedAt == null) detectedAt = LocalDateTime.now();
        if (delayMinutes < 0) delayMinutes = 0;
        if (passengers < 0) passengers = 0;
    }

    public static List<DisruptionEntity> findActive() {
        return list("resolved", false);
    }

    public static List<DisruptionEntity> findBySeverity(DisruptionSeverity severity) {
        return list("severity", severity);
    }

    public static List<DisruptionEntity> findByFlightId(UUID flightId) {
        return list("flightId", flightId);
    }
}
