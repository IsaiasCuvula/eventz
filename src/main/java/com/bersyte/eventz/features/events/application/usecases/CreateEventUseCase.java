package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
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
    private final IdGenerator idGenerator;
    
    public CreateEventUseCase(EventRepository repository, UserValidationService userValidationService, EventMapper mapper, IdGenerator idGenerator) {
        this.repository = repository;
        this.userValidationService = userValidationService;
        this.mapper = mapper;
        this.idGenerator = idGenerator;
    }
    
    @Override
    public EventResponse execute(CreateEventInput input) {
        CreateEventRequest request = input.request();
        String userEmail = input.userEmail();
        AppUser organizer = userValidationService.getAuthorizedOrganizer(userEmail);
        String eventId = idGenerator.generateUuid();
        Event event = mapper.toDomain(eventId, organizer, request);
        Event savedEvent = repository.createEvent(event);
        return mapper.toResponse(savedEvent);
    }
}
