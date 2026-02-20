package com.bersyte.eventz.features.events.infrastructure.services;

import com.bersyte.eventz.features.events.application.events.EventCreationEvent;
import com.bersyte.eventz.features.events.application.events.EventUpdatedEvent;
import com.bersyte.eventz.features.events.domain.services.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisherAdapter implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    @Override
    public void publishUpdateEvent(EventUpdatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
    
    @Override
    public void publishEventCreationEvent(EventCreationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
