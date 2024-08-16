package com.bersyte.eventz.events;

import com.bersyte.eventz.event_registration.Registration;
import com.bersyte.eventz.security.auth.AppUser;

import java.util.Date;
import java.util.List;


public class EventMappers {


   public static EventResponseDTO toResponseDTO(Event entity){
       final AppUser organizer = entity.getOrganizer();
       final List<Registration> participants = entity.getRegistrations();

       return new EventResponseDTO(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
               organizer.getFirstName() + " " + organizer.getLastName(),
               participants.stream().map(
                       participant -> participant.getUser().getFirstName() +
                               " " + participant.getUser().getLastName()
               ).toList(),
               entity.getCreatedAt()
       );
   }

    public static Event  toEventEntity(EventRequestDTO dto){
        Event entity = new Event();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setLocation(dto.location());
        entity.setDate(new Date(dto.date()));
        entity.setCreatedAt(new Date(dto.createdAt()));
        return  entity;
    }
}
