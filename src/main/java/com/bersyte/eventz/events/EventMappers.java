package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.event_participation.ParticipationStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class EventMappers {

    public EventResponseDto toResponseDTO(Event entity) {
       final AppUser organizer = entity.getOrganizer();
        final List<EventParticipation> registrations = entity.getParticipants () == null ?
                List.of () : entity.getParticipants ();

        return new EventResponseDto(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
                organizer.getFirstName () + " " + organizer.getLastName (),
               registrations.stream()
                       .filter(registration -> registration.getStatus() == ParticipationStatus.ACTIVE)
                       .map(
                               registration -> {
                                   AppUser user = registration.getUser();
                                   String lastName = user.getLastName() == null ? "" : user.getLastName();
                                   String firstName = user.getFirstName();
                                   return firstName + " " + lastName;
                               }
               ).toList(),
               entity.getCreatedAt()
       );
   }

    public Event toEventEntity(EventRequestDto dto) {
        Event entity = new Event();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setLocation(dto.location());
        entity.setDate(new Date(dto.date()));
        entity.setCreatedAt(new Date(dto.createdAt()));
        return  entity;
    }
}
