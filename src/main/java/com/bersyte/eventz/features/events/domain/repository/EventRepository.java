package com.bersyte.eventz.features.events.domain.repository;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.domain.model.Event;
import java.util.Date;
import java.util.Optional;

public interface EventRepository {
    Event createEvent(Event event);
    Event updateEvent(Event event);
    void deleteEvent(String id);
    Optional<Event> getEventById(String id);
    PagedResult<Event> getFilteredEventsByTitleAndLocation(
            String title, String location, Pagination pagination);
    PagedResult<Event> getEventsByDate(Pagination pagination, Date date);
    PagedResult<Event> fetchUpcomingEvents(Date date, Pagination pagination);
    PagedResult<Event> fetchEvents(Pagination pagination);
    boolean existsById(String id);
}
