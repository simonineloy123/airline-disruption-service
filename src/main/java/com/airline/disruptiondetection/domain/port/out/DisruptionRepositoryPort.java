package com.airline.disruptiondetection.domain.port.out;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.model.DisruptionSeverity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisruptionRepositoryPort {
    Disruption save(Disruption disruption);
    Optional<Disruption> findById(UUID id);
    List<Disruption> findAll();
    List<Disruption> findActive();
    List<Disruption> findBySeverity(DisruptionSeverity severity);
    List<Disruption> findByFlightId(UUID flightId);
}
