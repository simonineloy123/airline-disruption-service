package com.airline.disruptiondetection.application.usecase;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.model.DisruptionSeverity;
import com.airline.disruptiondetection.domain.port.in.GetDisruptionUseCase;
import com.airline.disruptiondetection.domain.port.out.DisruptionRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GetDisruptionUseCaseImpl implements GetDisruptionUseCase {

    private final DisruptionRepositoryPort disruptionRepository;

    public GetDisruptionUseCaseImpl(DisruptionRepositoryPort disruptionRepository) {
        this.disruptionRepository = disruptionRepository;
    }

    @Override
    public Optional<Disruption> findById(UUID id) {
        return disruptionRepository.findById(id);
    }

    @Override
    public List<Disruption> findAll() {
        return disruptionRepository.findAll();
    }

    @Override
    public List<Disruption> findActive() {
        return disruptionRepository.findActive();
    }

    @Override
    public List<Disruption> findBySeverity(DisruptionSeverity severity) {
        return disruptionRepository.findBySeverity(severity);
    }

    @Override
    public List<Disruption> findByFlightId(UUID flightId) {
        return disruptionRepository.findByFlightId(flightId);
    }
}
