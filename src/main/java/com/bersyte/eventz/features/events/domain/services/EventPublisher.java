package com.bersyte.eventz.features.events.domain.services;

import com.bersyte.eventz.features.events.application.events.EventCreationEvent;
import com.bersyte.eventz.features.events.application.events.EventUpdatedEvent;

public interface EventPublisher {
    void publishUpdateEvent(EventUpdatedEvent event);
    void publishEventCreationEvent(EventCreationEvent event);
}
