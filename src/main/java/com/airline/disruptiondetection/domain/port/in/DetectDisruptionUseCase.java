package com.airline.disruptiondetection.domain.port.in;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.model.FlightEventPayload;

public interface DetectDisruptionUseCase {
    void execute(FlightEventPayload event);
}
