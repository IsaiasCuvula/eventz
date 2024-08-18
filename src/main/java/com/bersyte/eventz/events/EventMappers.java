package com.bersyte.eventz.events;

import com.bersyte.eventz.event_registration.Registration;
import com.bersyte.eventz.event_registration.RegistrationStatus;
import com.bersyte.eventz.security.auth.AppUser;

import java.util.Date;
import java.util.List;


public class EventMappers {


   public static EventResponseDTO toResponseDTO(Event entity){
       final AppUser organizer = entity.getOrganizer();
       final List<Registration> registrations = entity.getRegistrations();
       final String organizerLastName = organizer.getLastName() == null ? " " : organizer.getLastName();

       return new EventResponseDTO(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
               organizer.getFirstName() + " " + organizerLastName,
               registrations.stream()
                       .filter(registration -> registration.getStatus() == RegistrationStatus.ACTIVE)
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
