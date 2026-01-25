package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.events.application.dtos.CreateEventInput;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class CreateEventUseCase implements UseCase<CreateEventInput, EventResponse> {
    private final EventRepository repository;
    private final UserValidationService userValidationService;
    private final EventMapper mapper;
    
    public CreateEventUseCase(EventRepository repository, UserValidationService userValidationService, EventMapper mapper) {
        this.repository = repository;
        this.userValidationService = userValidationService;
        this.mapper = mapper;
    }
    
    @Override
    public EventResponse execute(CreateEventInput input) {
        CreateEventRequest request = input.request();
        String organizerEmail = input.organizerEmail();
        AppUser organizer = userValidationService.validateAndGetUser(organizerEmail);
        
        if(organizer.hasPermissionToCreateEvents()){
            throw new UnauthorizedException("Insufficient permissions to create events");
        }
        
        Event event = mapper.toDomain(request);
        event.setOrganizer(organizer);
        Event savedEvent = repository.createEvent(event);
        return mapper.toResponse(savedEvent);
    }
}
