package com.bersyte.eventz.common;

import com.bersyte.eventz.events.*;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventCommonService {

    private final EventRepository eventRepository;


    public Event findEventById(Long id) {
        try {
            Optional<Event> eventOptional = eventRepository.findById(id);
            if (eventOptional.isEmpty()) {
                String errorMsg = String.format("Event with id: %s not found", id);
                throw new DatabaseOperationException(errorMsg);
            }
            return eventOptional.get();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get event by id %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
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
            return EventMappers.toResponseDTO(eventRepository.save(oldEvent));
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error while updating event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

}
