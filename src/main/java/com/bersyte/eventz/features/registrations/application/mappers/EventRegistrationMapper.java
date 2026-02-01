package com.bersyte.eventz.features.registrations.application.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.registrations.application.dtos.EventParticipantResponse;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.util.List;


public class EventRegistrationMapper {
    
    
    public PagedResult<EventParticipantResponse> toPagedParticipants(PagedResult<EventRegistration> pagedResult){
        List<EventParticipantResponse> participants = pagedResult.data().stream().map(
                this::toParticipant
        ).toList();
        return new PagedResult<>(
                participants,
                pagedResult.totalElements(),
                pagedResult.totalPages(), pagedResult.isLast()
        );
    }
    
    public EventParticipantResponse toParticipant(EventRegistration registration){
        AppUser user = registration.getUser();
        Event event = registration.getEvent();
        
        return new EventParticipantResponse(
                registration.getId(),
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                registration.getStatus(),
                registration.getCreatedAt(),
                registration.getUpdatedAt()
        );
    }
    
    public EventRegistration toDomain(String id, String checkInToken, Event event, AppUser user){
        return EventRegistration.create(id, checkInToken, event, user);
    }
    
    public TicketResponse toTicketResponse(EventRegistration registration){
        AppUser user = registration.getUser();
        Event event = registration.getEvent();
        
        return new TicketResponse(
                registration.getId(),
                user.getFullName(),
                user.getId(),
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                registration.getCheckInToken(),
                registration.getStatus(),
                registration.getCreatedAt(),
                registration.getUpdatedAt()
        );
    }
}
