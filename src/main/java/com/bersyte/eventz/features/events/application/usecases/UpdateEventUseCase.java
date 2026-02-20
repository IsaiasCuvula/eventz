package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.dtos.UpdateEventInput;
import com.bersyte.eventz.features.events.application.dtos.UpdateEventRequest;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.domain.services.EventValidationService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;


public class UpdateEventUseCase implements UseCase<UpdateEventInput, EventResponse> {
    private final EventRepository repository;
    private final EventMapper mapper;
    private final UserValidationService userValidationService;
    private final EventValidationService eventValidationService;
    private final Clock clock;
    
    public UpdateEventUseCase(EventRepository repository, EventMapper mapper, UserValidationService userValidationService, EventValidationService eventValidationService, Clock clock) {
        this.repository = repository;
        this.mapper = mapper;
        this.userValidationService = userValidationService;
        this.eventValidationService = eventValidationService;
        this.clock = clock;
    }
    
    @Transactional
    @Override
    public EventResponse execute(UpdateEventInput input) {
        UUID eventId = input.eventId();
        UUID requesterId = input.requesterId();
        UpdateEventRequest request  = input.request();
        
        if (isRequestEmpty(request)) {
            throw new IllegalArgumentException("At least one field must be completed for the update.");
        }
        AppUser requester = userValidationService.getAuthorizedOrganizerById(requesterId);
        Event event = eventValidationService.getValidEventById(eventId);
        
        if(!requester.isAdmin() && !event.isOwnedBy(requester)){
            throw new UnauthorizedException("You don't own this event");
        }
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        Event updatedDetails = event.updateDetails(request, updatedAt);
        
        Event savedUpdatedEvent = repository.updateEvent(updatedDetails);
        return mapper.toResponse(savedUpdatedEvent);
    }
    
    private boolean isRequestEmpty(UpdateEventRequest request){
        return request.title() == null && request.description() == null &&
        request.location() == null && request.date() == null && request.price() == null &&
        request.maxParticipants() == null;
    }
}
