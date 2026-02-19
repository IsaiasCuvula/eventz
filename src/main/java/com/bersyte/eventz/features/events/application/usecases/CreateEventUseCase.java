package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.services.IdGenerator;
import com.bersyte.eventz.features.events.application.dtos.CreateEventInput;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateEventUseCase implements UseCase<CreateEventInput, EventResponse> {
    private final EventRepository repository;
    private final UserValidationService userValidationService;
    private final EventMapper mapper;
    private final IdGenerator idGenerator;
    private final Clock clock;
    
    public CreateEventUseCase(EventRepository repository, UserValidationService userValidationService, EventMapper mapper, IdGenerator idGenerator, Clock clock) {
        this.repository = repository;
        this.userValidationService = userValidationService;
        this.mapper = mapper;
        this.idGenerator = idGenerator;
        this.clock = clock;
    }
    
    @Override
    public EventResponse execute(CreateEventInput input) {
        CreateEventRequest request = input.request();
        UUID requesterId = input.requesterId();
        AppUser organizer = userValidationService.getAuthorizedOrganizerById(requesterId);
        UUID eventId = idGenerator.generateUuid();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        Event event = Event.create(
                eventId,
                request.title(),
                request.description(),
                request.location(),
                request.date(),
                request.maxParticipants(),
                organizer,
                request.price(),
                createdAt
                
        );
        Event savedEvent = repository.createEvent(event);
        return mapper.toResponse(savedEvent);
    }
}
