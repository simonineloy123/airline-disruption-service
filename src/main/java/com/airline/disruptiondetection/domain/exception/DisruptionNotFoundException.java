package com.airline.disruptiondetection.domain.exception;

import java.util.UUID;

public class DisruptionNotFoundException extends RuntimeException {
    public DisruptionNotFoundException(UUID id) {
        super("Disrupción no encontrada con id: " + id);
    }
}
