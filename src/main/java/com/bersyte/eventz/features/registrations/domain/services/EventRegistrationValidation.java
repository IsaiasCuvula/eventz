package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;

public class EventRegistrationValidation {
    
    private final EventRegistrationRepository registrationRepository;
    
    public EventRegistrationValidation(EventRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }
    
    
    EventRegistration findValidRegistrationByEvent(String eventId){
        return registrationRepository.findByEventId(eventId)
                       .orElseThrow(()-> new ResourceNotFoundException("EventRegistration", eventId));
    }
}
