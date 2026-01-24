package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class CreateEventUseCase implements UseCase<CreateEventRequest, EventResponse> {
    private final EventRepository repository;
    private final EventMapper mapper;
    
    public CreateEventUseCase(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public EventResponse execute(CreateEventRequest request) {
        Event event = mapper.toDomain(request);
        Event savedEvent = repository.createEvent(event);
        return mapper.toResponse(savedEvent);
    }
}
