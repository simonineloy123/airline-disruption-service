package com.airline.disruptiondetection.shared.mapper;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.infrastructure.adapter.out.persistence.DisruptionEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisruptionMapper {

    public Disruption toDomain(DisruptionEntity entity) {
        return Disruption.builder()
            .id(entity.id)
            .flightId(entity.flightId)
            .flightNumber(entity.flightNumber)
            .origin(entity.origin)
            .destination(entity.destination)
            .type(entity.type)
            .severity(entity.severity)
            .description(entity.description)
            .delayMinutes(entity.delayMinutes)
            .detectedAt(entity.detectedAt)
            .passengers(entity.passengers)
            .resolvedAt(entity.resolvedAt)
            .resolved(entity.resolved)
            .build();
    }

    public DisruptionEntity toEntity(Disruption domain) {
        DisruptionEntity entity = new DisruptionEntity();
        entity.flightId     = domain.getFlightId();
        entity.flightNumber = domain.getFlightNumber();
        entity.origin       = domain.getOrigin();
        entity.destination  = domain.getDestination();
        entity.type         = domain.getType();
        entity.severity     = domain.getSeverity();
        entity.description  = domain.getDescription();
        entity.delayMinutes = domain.getDelayMinutes();
        entity.detectedAt   = domain.getDetectedAt();
        entity.passengers = domain.getPassengers();
        entity.resolvedAt   = domain.getResolvedAt();
        entity.resolved     = domain.isResolved();
        return entity;
    }
}
