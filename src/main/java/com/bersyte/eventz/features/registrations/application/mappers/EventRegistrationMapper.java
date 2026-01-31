package com.bersyte.eventz.features.registrations.application.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.registrations.application.dtos.EventParticipantResponse;
import com.bersyte.eventz.features.registrations.application.dtos.EventRegistrationResponse;
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
                registration.getUpdateAt()
        );
    }
    
    public EventRegistration toDomain(Event event, AppUser user, String id){
        return EventRegistration.create(id, event, user);
    }
    
    public EventRegistrationResponse toResponse(EventRegistration registration){
        
        return new EventRegistrationResponse(
                registration.getId(),
                registration.getUser().getFullName(),
                registration.getUser().getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getEvent().getDescription(),
                registration.getStatus(),
                registration.getCreatedAt(),
                registration.getUpdateAt()
        );
    }
}
