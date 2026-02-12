package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.events.application.dtos.EventByIdInput;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.application.mappers.EventMapper;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class GetEventByIdUseCase implements UseCase<EventByIdInput, EventResponse> {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    
    public GetEventByIdUseCase(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }
    
    @Override
    public EventResponse execute(EventByIdInput input) {
        
        Event event = eventRepository.findEventById(input.eventId())
                              .orElseThrow(
                                      ()-> new ResourceNotFoundException("Event", input.eventId())
                              );
        return eventMapper.toResponse(event);
    }
}
