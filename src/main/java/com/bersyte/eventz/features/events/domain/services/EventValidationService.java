package com.bersyte.eventz.features.events.domain.services;

import com.bersyte.eventz.features.events.domain.exceptions.EventNotFoundException;
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
                       .orElseThrow(()-> new EventNotFoundException("Event with id: " + eventId + " not found."));
        
    }
}
