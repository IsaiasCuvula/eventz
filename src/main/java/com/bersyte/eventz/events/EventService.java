package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;
    private final UserService usersService;
    private final EventCommonService eventCommonService;


    public EventResponseDto createEvent(
            EventRequestDto data, UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            AppUser currentUser = usersService.getUserByEmail(email);
            Event event = EventMappers.toEventEntity(data);
            event.setOrganizer(currentUser);

            Event newEvent = repository.save(event);
            return EventMappers.toResponseDTO(newEvent);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error creating new event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public List<EventResponseDto> getAllEvents(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventsPerPage = repository.findAll(pageable);
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error getting all events: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public List<EventResponseDto> getUpcomingEvents(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventsPerPage = repository.getUpcomingEvents(new Date(), pageable);
            return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error getting upcoming events: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public EventResponseDto getEventById(Long id) {
        try {
            Event event = eventCommonService.findEventById(id);
            return EventMappers.toResponseDTO(event);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error while updating event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public EventResponseDto updateEvent(
            Long id,
            EventRequestDto data,
            UserDetails userDetails
    ) {

        try {
            String email = userDetails.getUsername();
            Event event = eventCommonService.findEventById(id);

            if (event.getOrganizer().getEmail().equals(email)) {
                return eventCommonService.updateEventOnDb(event, data);
            } else {
                throw new AccessDeniedException("You do not have permission to update this event");
            }
        } catch (DataAccessException e) {
            String errorMsg = String.format("Error while updating event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }


    public void deleteEvent(Long id, UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            Event event = eventCommonService.findEventById(id);
            if (event.getOrganizer().getEmail().equals(email)) {
                repository.delete(event);
            } else {
                throw new AccessDeniedException("You do not have permission to delete this event");
            }
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to delete event: %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public List<EventResponseDto> filterEvents(
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

    public List<EventResponseDto> getEventsByDate(int page, int size, Long date) {
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
