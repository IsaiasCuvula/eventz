package com.bersyte.eventz.features.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.features.event_participation.EventParticipation;
import com.bersyte.eventz.features.event_participation.ParticipationStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class EventMappers {

    public EventResponseDto toResponseDTO(EventEntity entity) {
        if (entity == null) {
            throw new NullPointerException ("Entity cannot be null");
        }
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
                                   return user.getFirstName () + " " + user.getLastName ();
                               }
               ).toList(),
               entity.getCreatedAt()
       );
   }

    public EventEntity toEventEntity(EventRequestDto dto) {
        if (dto == null) {
            throw new NullPointerException ("Request data cannot be null");
        }
        EventEntity entity = new EventEntity();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setLocation(dto.location());
        entity.setDate(new Date(dto.date()));
        entity.setCreatedAt(new Date(dto.createdAt()));
        return  entity;
    }
}
