package com.airline.disruptiondetection.infrastructure.adapter.out.messaging;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.port.out.DisruptionEventPublisherPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DisruptionEventPublisherAdapter implements DisruptionEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(DisruptionEventPublisherAdapter.class);

    private final Emitter<String> disruptionEventsEmitter;
    private final ObjectMapper    objectMapper;

    public DisruptionEventPublisherAdapter(
        @Channel("disruption-events") Emitter<String> disruptionEventsEmitter
    ) {
        this.disruptionEventsEmitter = disruptionEventsEmitter;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }

    @Override
    public void publish(Disruption disruption) {
        try {
            String payload = buildPayload(disruption);
            disruptionEventsEmitter.send(payload);
            LOG.infof("Disrupción publicada → topic: disruption_events | vuelo: %s | tipo: %s | severidad: %s | pasajeros: %d",
                disruption.getFlightNumber(), disruption.getType(),
                disruption.getSeverity(), disruption.getPassengers());
        } catch (JsonProcessingException e) {
            LOG.errorf("Error serializando disrupción: %s", e.getMessage());
            throw new RuntimeException("Error publicando disrupción a Kafka", e);
        }
    }

    private String buildPayload(Disruption disruption) throws JsonProcessingException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id",           disruption.getId().toString());
        node.put("flightId",     disruption.getFlightId().toString());
        node.put("flightNumber", disruption.getFlightNumber());
        node.put("origin",       disruption.getOrigin());
        node.put("destination",  disruption.getDestination());
        node.put("type",         disruption.getType().name());
        node.put("severity",     disruption.getSeverity().name());
        node.put("description",  disruption.getDescription());
        node.put("delayMinutes", disruption.getDelayMinutes());
        node.put("passengers",   disruption.getPassengers());
        node.put("detectedAt",   disruption.getDetectedAt().toString());
        node.put("resolved",     disruption.isResolved());
        return objectMapper.writeValueAsString(node);
    }
}
