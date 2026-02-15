package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.features.registrations.application.events.CheckinEvent;
import com.bersyte.eventz.features.registrations.application.events.EventRegistrationEvent;
import com.bersyte.eventz.features.registrations.application.events.TicketCancellationEvent;
import com.bersyte.eventz.features.registrations.application.events.UpdateCheckinTokenEvent;

public interface EventRegistrationPublisher {
    
    void publishJoinEvent(EventRegistrationEvent event);
    void publishUpdateCheckinToken(UpdateCheckinTokenEvent event);
    void publishCheckin(CheckinEvent event);
    void publishTicketCancellation(TicketCancellationEvent event);
}
