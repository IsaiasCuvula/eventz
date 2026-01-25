package com.bersyte.eventz.features.events.application.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.domain.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    
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
    
    public Event toDomain(CreateEventRequest request){
        return new Event(
                request.title(),
                request.description(),
                request.location(),
                request.date(),
                request.maxParticipants()
        );
    }
    
    public EventResponse toResponse(Event event){
        List<String> participants = new ArrayList<>();
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                "Organizer",
                event.getDate(),
                participants,
                event.getCreatedAt(),
                event.getUpdateAt()
        );
    }
}
