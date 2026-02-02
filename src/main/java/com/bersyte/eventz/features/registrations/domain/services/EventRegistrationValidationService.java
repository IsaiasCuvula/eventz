package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;

public class EventRegistrationValidationService {
    
    private final EventRegistrationRepository registrationRepository;
    
    public EventRegistrationValidationService(EventRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }
    
    
    public EventRegistration getValidRegistrationByCheckInToken(String token){
        return registrationRepository.findRegistrationByCheckInToken(
                token
        ).orElseThrow(()-> new ResourceNotFoundException("Ticket", token));
    }
    
    
    public EventRegistration getValidRegistrationById(String registrationId){
        return registrationRepository.findById(registrationId)
                       .orElseThrow(()-> new ResourceNotFoundException("Event Registration", registrationId));
    }
    
    public EventRegistration getValidActiveRegistration(String eventId, String userId){
        return getValidRegistrationByStatus(eventId, userId, RegistrationStatus.ACTIVE);
    }
    
    
    private EventRegistration getValidRegistrationByStatus(String eventId, String userId, RegistrationStatus status){
        return registrationRepository.findUserRegistrationByStatus(eventId,userId, status)
                       .orElseThrow(()-> new ResourceNotFoundException("Event Registration", eventId));
    }
}
