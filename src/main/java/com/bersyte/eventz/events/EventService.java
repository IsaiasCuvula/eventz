package com.bersyte.eventz.events;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;


    public EventResponseDTO createEvent(EventRequestDTO data) {
        Event newEvent = EventMappers.toEventEntity(data);
        try {
           repository.save(newEvent);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error creating new event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
        return EventMappers.toResponseDTO(newEvent);
    }

    public List<EventResponseDTO> getAllEvents(int page, int size){
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventsPerPage = repository.findAll(pageable);
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error getting all events: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventsPerPage = repository.getUpcomingEvents(new Date(), pageable);
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error getting upcoming events: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public EventResponseDTO getEventById(Integer id){
       Event event = this.findEventById(id);
       return EventMappers.toResponseDTO(event);
    }

    public EventResponseDTO updateEvent(Integer id, EventRequestDTO data) {
        try {
            Event event = this.findEventById(id);

            if (data.title() != null) {
                event.setTitle(data.title());
            }
            if (data.description() != null) {
                event.setDescription(data.description());
            }
            if (data.location() != null) {
                event.setLocation(data.location());
            }
            if (data.date() != null) {
                event.setDate(new Date(data.date()));
            }
            //
            return EventMappers.toResponseDTO(repository.save(event));
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error while updating event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
         }

    }


    public void deleteEvent(Integer id){
        try {
            Event eventOptional = this.findEventById(id);
            repository.delete(eventOptional);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to delete event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    private Event findEventById(Integer id){
        try {
            Optional<Event> eventOptional = repository.findById(id);
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

    public List<EventResponseDTO> filterEvents(
        int page, 
        int size,
        String title, 
        String location
    ){
        try {
            title = (title != null) ? title : " ";
            location = (location != null) ? location : " ";
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventsPerPage = this.repository.findFilteredEvents(
                    title, location, pageable
            );
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get filtered events: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public List<EventResponseDTO> getEventsByDate(int page, int size, Long date) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Date dateEvent = (date != null) ? new Date(date) : new Date();
            Page<Event> eventsPerPage = this.repository.getEventsByDate(dateEvent, pageable);
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get event by date: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }


}
