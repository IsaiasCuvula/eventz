package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.event_participation.ParticipationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventMappers {


    public static EventResponseDto toResponseDTO(Event entity) {
       final AppUser organizer = entity.getOrganizer();
       final List<EventParticipation> registrations = entity.getRegistrations() == null ?
               new ArrayList<>() : entity.getRegistrations();

       final String organizerLastName = organizer.getLastName() == null ? " " : organizer.getLastName();

        return new EventResponseDto(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
               organizer.getFirstName() + " " + organizerLastName,
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

    public static Event toEventEntity(EventRequestDto dto) {
        Event entity = new Event();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setLocation(dto.location());
        entity.setDate(new Date(dto.date()));
        entity.setCreatedAt(new Date(dto.createdAt()));
        return  entity;
    }
}
