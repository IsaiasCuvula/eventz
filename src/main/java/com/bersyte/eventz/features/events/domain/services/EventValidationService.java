package com.bersyte.eventz.features.events.domain.services;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

import java.util.UUID;

public class EventValidationService {
    private final EventRepository eventRepository;
    
    public EventValidationService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    public Event getValidEventById(UUID eventId){
        return eventRepository.findEventById(eventId)
                       .orElseThrow(()-> new ResourceNotFoundException("Event", eventId));
        
    }
}
