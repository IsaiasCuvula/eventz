package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.registrations.application.dtos.DeleteEventRegistrationRequest;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.domain.services.EventRegistrationValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

public class DeleteRegistrationUseCase implements VoidUseCase<DeleteEventRegistrationRequest> {
    private final UserValidationService userValidationService;
    private final EventRegistrationRepository registrationRepository;
    private final EventRegistrationValidationService registrationValidationService;
    private final EventRepository eventRepository;
    
    public DeleteRegistrationUseCase(UserValidationService userValidationService, EventRegistrationRepository registrationRepository, EventRegistrationValidationService registrationValidationService, EventRepository eventRepository) {
        this.userValidationService = userValidationService;
        this.registrationRepository = registrationRepository;
        this.registrationValidationService = registrationValidationService;
        this.eventRepository = eventRepository;
    }
    
    @Transactional
    @Override
    public void execute(DeleteEventRegistrationRequest request) {
        String requesterEmail = request.requesterEmail();
        String registrationId = request.registrationId();
        
        AppUser requester = userValidationService.getRequester(requesterEmail);
        EventRegistration registration = registrationValidationService.getValidRegistrationById(registrationId);
        
        if(!registration.canEditOrDelete(requester)){
            throw new UnauthorizedException("You don't have permission");
        }
        
        // reverter payment only if follow certain condition (e.g.: week or month before the event)
        
        registrationRepository.delete(registration);
        eventRepository.decrementParticipantCount(registration.getEvent().getId());
        
        //Send cancellation email
        
    }
}
