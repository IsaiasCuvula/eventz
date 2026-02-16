package com.bersyte.eventz.features.events.domain.repository;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.events.domain.model.Event;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository {
    //Admin & Organizer
    Event createEvent(Event event);
    Event updateEvent(Event event);
    void deleteEvent(String id);
    PagedResult<Event> fetchEventsByOrganizer(String organizerId, Pagination pagination);
    
    //End user (public)
    Optional<Event> findEventById(String id);
    PagedResult<Event> fetchEvents(Pagination pagination);
    PagedResult<Event> fetchEventsByDate(Pagination pagination, LocalDateTime startDate,LocalDateTime endDate);
    PagedResult<Event> fetchUpcomingEvents(LocalDateTime now, Pagination pagination);
    PagedResult<Event> filterEvents(String title, String location, Pagination pagination);
    
    //Update counters
    void incrementParticipantCount(String eventId);
    void decrementParticipantCount(String eventId);
}



