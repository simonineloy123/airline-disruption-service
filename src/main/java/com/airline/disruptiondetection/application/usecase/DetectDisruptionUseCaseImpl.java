package com.airline.disruptiondetection.application.usecase;

import com.airline.disruptiondetection.domain.model.*;
import com.airline.disruptiondetection.domain.port.in.DetectDisruptionUseCase;
import com.airline.disruptiondetection.domain.port.out.DisruptionEventPublisherPort;
import com.airline.disruptiondetection.domain.port.out.DisruptionRepositoryPort;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DetectDisruptionUseCaseImpl implements DetectDisruptionUseCase {

    private static final Logger LOG = Logger.getLogger(DetectDisruptionUseCaseImpl.class);
    private static final int DELAY_THRESHOLD_MINUTES = 15;

    private final DisruptionRepositoryPort     disruptionRepository;
    private final DisruptionEventPublisherPort eventPublisher;
    private final Event<Disruption>            disruptionEvent;
    private final Vertx                        vertx;

    public DetectDisruptionUseCaseImpl(
        DisruptionRepositoryPort disruptionRepository,
        DisruptionEventPublisherPort eventPublisher,
        Event<Disruption> disruptionEvent,
        Vertx vertx
    ) {
        this.disruptionRepository = disruptionRepository;
        this.eventPublisher       = eventPublisher;
        this.disruptionEvent      = disruptionEvent;
        this.vertx                = vertx;
    }

    @Override
    public void execute(FlightEventPayload event) {
        LOG.debugf("Procesando evento: %s para vuelo: %s",
            event.getEventType(), event.getFlightNumber());

        detectDisruption(event).ifPresent(disruption ->
            vertx.executeBlocking(() -> {
                saveAndNotify(disruption);
                return null;
            })
        );
    }

    private Optional<Disruption> detectDisruption(FlightEventPayload event) {
        return switch (event.getEventType()) {
            case "flight_delayed" -> {
                if (event.getDelayMinutes() >= DELAY_THRESHOLD_MINUTES) {
                    yield Optional.of(buildDisruption(event,
                        DisruptionType.DELAY,
                        DisruptionSeverity.fromDelayMinutes(event.getDelayMinutes()),
                        String.format("Vuelo %s retrasado %d minutos",
                            event.getFlightNumber(), event.getDelayMinutes())
                    ));
                }
                LOG.debugf("Retraso menor al umbral (%d min), ignorando", event.getDelayMinutes());
                yield Optional.empty();
            }
            case "flight_cancelled" -> Optional.of(buildDisruption(event,
                DisruptionType.CANCELLATION,
                DisruptionSeverity.CRITICAL,
                String.format("Vuelo %s cancelado", event.getFlightNumber())
            ));
            default -> {
                LOG.debugf("Evento %s no genera disrupción", event.getEventType());
                yield Optional.empty();
            }
        };
    }

    private Disruption buildDisruption(
        FlightEventPayload event,
        DisruptionType type,
        DisruptionSeverity severity,
        String description
    ) {
        return Disruption.builder()
            .flightId(UUID.fromString(event.getAggregateId()))
            .flightNumber(event.getFlightNumber())
            .origin(event.getOrigin())
            .destination(event.getDestination())
            .type(type)
            .severity(severity)
            .description(description)
            .delayMinutes(event.getDelayMinutes())
                .passengers(event.getPassengers())
            .build();
    }

    @Transactional
    public void saveAndNotify(Disruption disruption) {
        Disruption saved = disruptionRepository.save(disruption);
        LOG.infof("Disrupción detectada: %s | vuelo: %s | severidad: %s",
            saved.getType(), saved.getFlightNumber(), saved.getSeverity());
        disruptionEvent.fire(saved);
    }

    public void onDisruptionSaved(
        @Observes(during = TransactionPhase.AFTER_SUCCESS) Disruption disruption
    ) {
        eventPublisher.publish(disruption);
    }
}
