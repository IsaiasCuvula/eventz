package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.events.application.dtos.EventByIdRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.exceptions.EventNotFoundException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class GetEventByIdUseCase implements UseCase<EventByIdRequest, EventResponse> {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    
    public GetEventByIdUseCase(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }
    
    @Override
    public EventResponse execute(EventByIdRequest request) {
        
        Event event = eventRepository.findEventById(request.eventId())
                              .orElseThrow(
                                      ()-> new EventNotFoundException(request.eventId())
                              );
        return eventMapper.toResponse(event);
    }
}
