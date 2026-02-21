package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationNotFoundException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;

import java.util.UUID;

public class EventRegistrationValidationService {
    
    private final EventRegistrationRepository registrationRepository;
    
    public EventRegistrationValidationService(EventRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }
    
    
    public EventRegistration getValidRegistrationByCheckInToken(String token){
        return registrationRepository.findRegistrationByCheckInToken(
                token
        ).orElseThrow(()-> new EventRegistrationNotFoundException("Ticket with token " + token + " not found"));
    }
    
    
    public EventRegistration getValidRegistrationById(UUID registrationId){
        return registrationRepository.findById(registrationId)
                       .orElseThrow(()-> new EventRegistrationNotFoundException("Event Registration with id: " + registrationId + " not found"));
    }
    
    public EventRegistration getValidActiveRegistration(UUID eventId, UUID userId){
        return getValidRegistrationByStatus(eventId, userId, RegistrationStatus.ACTIVE);
    }
    
    
    private EventRegistration getValidRegistrationByStatus(UUID eventId, UUID userId, RegistrationStatus status){
        return registrationRepository.findUserRegistrationByStatus(eventId,userId, status)
                       .orElseThrow(()-> new EventRegistrationNotFoundException("Event Registration with id: " + userId + " not found"));
    }
}
