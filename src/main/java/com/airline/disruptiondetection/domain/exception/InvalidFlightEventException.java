package com.airline.disruptiondetection.domain.exception;

public class InvalidFlightEventException extends RuntimeException {
    public InvalidFlightEventException(String message) {
        super("Evento de vuelo inválido: " + message);
    }
}
