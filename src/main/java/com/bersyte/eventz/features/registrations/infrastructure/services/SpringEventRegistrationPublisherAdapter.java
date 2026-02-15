package com.bersyte.eventz.features.registrations.infrastructure.services;

import com.bersyte.eventz.features.registrations.application.events.CheckinEvent;
import com.bersyte.eventz.features.registrations.application.events.EventRegistrationEvent;
import com.bersyte.eventz.features.registrations.application.events.TicketCancellationEvent;
import com.bersyte.eventz.features.registrations.application.events.UpdateCheckinTokenEvent;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventRegistrationPublisherAdapter implements EventRegistrationPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public SpringEventRegistrationPublisherAdapter(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public void publishJoinEvent(EventRegistrationEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    @Override
    public void publishUpdateCheckinToken(UpdateCheckinTokenEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    @Override
    public void publishCheckin(CheckinEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    @Override
    public void publishTicketCancellation(TicketCancellationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
