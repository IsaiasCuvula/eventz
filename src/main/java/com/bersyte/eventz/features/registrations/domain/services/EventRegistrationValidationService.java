package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;

public class EventRegistrationValidationService {
    
    private final EventRegistrationRepository registrationRepository;
    
    public EventRegistrationValidationService(EventRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }
    
    
    public EventRegistration getValidRegistrationById(String registrationId){
        return registrationRepository.findById(registrationId)
                       .orElseThrow(()-> new ResourceNotFoundException("Event Registration", registrationId));
    }
    
    
    public EventRegistration getValidRegistrationByEvent(String eventId){
        return registrationRepository.findByEventId(eventId)
                       .orElseThrow(()-> new ResourceNotFoundException("Event Registration", eventId));
    }
}
