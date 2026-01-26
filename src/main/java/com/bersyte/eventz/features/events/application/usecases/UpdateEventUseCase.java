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


public class UpdateEventUseCase implements UseCase<UpdateEventInput, EventResponse> {
    private final EventRepository repository;
    private final EventMapper mapper;
    private final UserValidationService userValidationService;
    private final EventValidationService eventValidationService;
    
    public UpdateEventUseCase(EventRepository repository, EventMapper mapper, UserValidationService userValidationService, EventValidationService eventValidationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userValidationService = userValidationService;
        this.eventValidationService = eventValidationService;
    }
    
    @Override
    public EventResponse execute(UpdateEventInput input) {
        String eventId = input.eventId();
        String userEmail = input.userEmail();
        UpdateEventRequest request  = input.request();
        
        if (isRequestEmpty(request)) {
            throw new IllegalArgumentException("At least one field must be completed for the update.");
        }
        AppUser user = userValidationService.getAuthorizedOrganizer(userEmail);
        Event event = eventValidationService.getValidEventById(eventId);
        
        if(!user.isAdmin() && !event.isOwnedBy(user)){
            throw new UnauthorizedException("You don't own this event");
        }

        if(request.title() != null) event.setTitle(request.title());
        if(request.description() != null) event.setDescription(request.description());
        if(request.location() != null) event.setLocation(request.location());
        if(request.date() != null) event.setDate(request.date());
        if(request.maxParticipants() != null) event.setMaxParticipants(request.maxParticipants());
        Event updatedEvent = repository.updateEvent(event);
        return mapper.toResponse(updatedEvent);
    }
    
    private boolean isRequestEmpty(UpdateEventRequest request){
        return request.title() == null &&
        request.description() == null &&
        request.location() == null &&
        request.date() == null &&
        request.maxParticipants() == null;
    }
}
