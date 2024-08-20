package com.bersyte.eventz.admin.events;

import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventRepository;
import com.bersyte.eventz.events.EventRequestDto;
import com.bersyte.eventz.events.EventResponseDto;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository repository;
    private final EventCommonService eventCommonService;


    public EventResponseDto updateEvent(Long id, EventRequestDto data) {
        try {
            Event event = eventCommonService.findEventById(id);
            return eventCommonService.updateEventOnDb(event, data);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error while updating event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public void deleteEvent(Long id) {
        try {
            Event event = eventCommonService.findEventById(id);
            repository.delete(event);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to delete event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

}
