package com.airline.disruptiondetection.domain.port.out;

import com.airline.disruptiondetection.domain.model.Disruption;

public interface DisruptionEventPublisherPort {
    void publish(Disruption disruption);
}
