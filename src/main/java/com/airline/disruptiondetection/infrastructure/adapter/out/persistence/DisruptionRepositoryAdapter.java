package com.airline.disruptiondetection.infrastructure.adapter.out.persistence;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.model.DisruptionSeverity;
import com.airline.disruptiondetection.domain.port.out.DisruptionRepositoryPort;
import com.airline.disruptiondetection.shared.mapper.DisruptionMapper;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DisruptionRepositoryAdapter implements DisruptionRepositoryPort {

    private final DisruptionMapper mapper;

    public DisruptionRepositoryAdapter(DisruptionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Disruption save(Disruption disruption) {
        DisruptionEntity entity = mapper.toEntity(disruption);
        DisruptionEntity.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Disruption> findById(UUID id) {
        return DisruptionEntity.<DisruptionEntity>findByIdOptional(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Disruption> findAll() {
        return DisruptionEntity.<DisruptionEntity>listAll()
            .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Disruption> findActive() {
        return DisruptionEntity.findActive()
            .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Disruption> findBySeverity(DisruptionSeverity severity) {
        return DisruptionEntity.findBySeverity(severity)
            .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Disruption> findByFlightId(UUID flightId) {
        return DisruptionEntity.findByFlightId(flightId)
            .stream().map(mapper::toDomain).toList();
    }
}
