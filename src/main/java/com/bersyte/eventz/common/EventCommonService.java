package com.bersyte.eventz.common;

import com.bersyte.eventz.events.*;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventCommonService {

    private final EventRepository eventRepository;
    private final EventMappers eventMappers;

    public EventCommonService(EventRepository eventRepository, EventMappers eventMappers) {
        this.eventRepository = eventRepository;
        this.eventMappers = eventMappers;
    }


    public Event findEventById(Long id) {
        try {
            return eventRepository.findById (id).orElseThrow (
                    () -> new DatabaseOperationException ("Could not find event with id: " + id)
            );
        } catch (DataAccessException e) {
            throw new DatabaseOperationException (
                    "Error accessing database: " + e.getLocalizedMessage ()
            );
        }
    }


    public EventResponseDto updateEventOnDb(Event oldEvent, EventRequestDto data) {
        try {
            if (data.title() != null) {
                oldEvent.setTitle(data.title());
            }
            if (data.description() != null) {
                oldEvent.setDescription(data.description());
            }
            if (data.location() != null) {
                oldEvent.setLocation(data.location());
            }
            if (data.date() != null) {
                oldEvent.setDate(new Date(data.date()));
            }
            //
            return eventMappers.toResponseDTO (eventRepository.save (oldEvent));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException (
                    "Error accessing database: " + e.getLocalizedMessage ()
            );
        }
    }

}
