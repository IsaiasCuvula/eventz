package com.bersyte.eventz.features.events.application.mappers;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventOrganizer;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.util.List;

public class EventMapper {
    
    
    
    public Event toDomain(String eventId, AppUser organizer, CreateEventRequest request){
        return Event.create(
                eventId,
                request.title(),
                request.description(),
                request.location(),
                request.date(),
                request.maxParticipants(),
                organizer
        );
    }
    
    public PagedResult<EventResponse> toPagedResponse(PagedResult<Event> eventPagedResult){
          List<EventResponse> eventResponseList = eventPagedResult.data().stream().map(
                  this::toResponse
          ).toList();
        return new PagedResult<>(
                eventResponseList,
                eventPagedResult.totalElements(),
                eventPagedResult.totalPages(), eventPagedResult.isLast()
        );
    }
    
    public EventResponse toResponse(Event event){
        EventOrganizer organizer =this.toEventOrganizer(event.getOrganizer());
        
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getParticipantsCount(),
                organizer,
                event.getCreatedAt(),
                event.getUpdateAt()
        );
    }
    
    private EventOrganizer toEventOrganizer(AppUser user) {
        if (user == null) return null;
        return new EventOrganizer(
                user.getFullName(),
                user.getEmail(),
                user.getId()
        );
    }
}
