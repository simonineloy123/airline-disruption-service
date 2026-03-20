package com.airline.disruptiondetection.infrastructure.adapter.in.messaging;

import com.airline.disruptiondetection.domain.model.FlightEventPayload;
import com.airline.disruptiondetection.domain.port.in.DetectDisruptionUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class FlightEventConsumer {

    private static final Logger LOG = Logger.getLogger(FlightEventConsumer.class);

    private final DetectDisruptionUseCase detectDisruptionUseCase;
    private final ObjectMapper            objectMapper;

    public FlightEventConsumer(DetectDisruptionUseCase detectDisruptionUseCase) {
        this.detectDisruptionUseCase = detectDisruptionUseCase;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }

    @Incoming("flight-events-in")
    public void consume(String message) {
        try {
            LOG.debugf("Evento recibido de Kafka: %s", message);
            FlightEventPayload payload = objectMapper.readValue(message, FlightEventPayload.class);
            detectDisruptionUseCase.execute(payload);
        } catch (Exception e) {
            LOG.errorf("Error procesando evento de vuelo: %s | error: %s",
                message, e.getMessage());
        }
    }
}
