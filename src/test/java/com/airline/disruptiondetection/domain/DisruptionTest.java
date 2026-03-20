package com.airline.disruptiondetection.domain;

import com.airline.disruptiondetection.domain.model.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class DisruptionTest {

    @Test
    void shouldCreateDisruptionWithDefaults() {
        Disruption d = Disruption.builder()
            .flightId(UUID.randomUUID())
            .flightNumber("AR1234")
            .origin("EZE")
            .destination("MAD")
            .type(DisruptionType.DELAY)
            .description("Retraso por clima")
            .delayMinutes(90)
            .passengers(200)
            .build();

        assertNotNull(d.getId());
        assertNotNull(d.getDetectedAt());
        assertFalse(d.isResolved());
        assertNull(d.getResolvedAt());
    }

    @Test
    void shouldDetectCriticalDisruption() {
        Disruption d = Disruption.builder()
            .flightId(UUID.randomUUID())
            .flightNumber("AR5678")
            .origin("EZE")
            .destination("MAD")
            .type(DisruptionType.CANCELLATION)
            .description("Vuelo cancelado")
            .delayMinutes(0)
            .passengers(300)
            .build();

        assertTrue(d.isCritical());
    }

    @Test
    void shouldResolveDisruption() {
        Disruption d = Disruption.builder()
            .flightId(UUID.randomUUID())
            .flightNumber("LA9999")
            .origin("SCL")
            .destination("GRU")
            .type(DisruptionType.DELAY)
            .description("Retraso menor")
            .delayMinutes(20)
            .passengers(100)
            .build();

        assertFalse(d.isResolved());
        d.resolve();
        assertTrue(d.isResolved());
        assertNotNull(d.getResolvedAt());
    }

    @Test
    void shouldCalculateSeverityFromDelay() {
        Disruption low = Disruption.builder()
            .flightId(UUID.randomUUID()).flightNumber("X001")
            .origin("EZE").destination("SCL")
            .type(DisruptionType.DELAY).description("test")
            .delayMinutes(20).passengers(50).build();

        Disruption high = Disruption.builder()
            .flightId(UUID.randomUUID()).flightNumber("X002")
            .origin("EZE").destination("MAD")
            .type(DisruptionType.DELAY).description("test")
            .delayMinutes(200).passengers(50).build();

        assertEquals(DisruptionSeverity.LOW,  low.getSeverity());
        assertEquals(DisruptionSeverity.HIGH, high.getSeverity());
    }

    @Test
    void shouldFailWithoutRequiredFields() {
        assertThrows(IllegalArgumentException.class, () ->
            Disruption.builder()
                .flightNumber("AR0000")
                .type(DisruptionType.DELAY)
                .build()
        );
    }
}