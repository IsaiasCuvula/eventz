package com.bersyte.eventz.features.registrations.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.registrations.application.dtos.EventParticipantResponse;
import com.bersyte.eventz.features.registrations.application.dtos.FetchEventParticipantsRequest;
import com.bersyte.eventz.features.registrations.application.mappers.EventRegistrationMapper;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.util.UUID;

public class FetchEventParticipantsUseCase implements UseCase<FetchEventParticipantsRequest, PagedResult<EventParticipantResponse>> {
    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserValidationService userValidationService;
    private final EventRegistrationMapper registrationMapper;
    private final EventValidationService eventValidationService;
    
    public FetchEventParticipantsUseCase(
            EventRegistrationRepository eventRegistrationRepository,
            UserValidationService userValidationService,
            EventRegistrationMapper registrationMapper, EventValidationService eventValidationService
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.userValidationService = userValidationService;
        this.registrationMapper = registrationMapper;
        this.eventValidationService = eventValidationService;
    }
    
    @Override
    public PagedResult<EventParticipantResponse> execute(FetchEventParticipantsRequest request) {
        UUID eventId = request.eventId();
        AppUser requester = userValidationService.getRequesterById(request.requesterId());
        
        if(!requester.canManageEvents()){
            throw new UnauthorizedException("You don't have permission");
        }
        Event event = eventValidationService.getValidEventById(eventId);
        
        if(!requester.isAdmin() && !event.isOwnedBy(requester)){
            throw new UnauthorizedException("You don't have permission");
        }
        PagedResult<EventRegistration>  result = eventRegistrationRepository.fetchParticipants(
                eventId,request.pagination()
        );
        return registrationMapper.toPagedParticipants(result);
    }
}
