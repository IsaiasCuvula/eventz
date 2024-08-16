package com.bersyte.eventz.event_registration;

import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventMappers;
import com.bersyte.eventz.security.auth.AppUser;

import java.util.Date;


public class RegistrationMapper {

    public static RegistrationResponseDTO toResponseDTO(Registration registration) {
        return new RegistrationResponseDTO(
                registration.getId(),
                EventMappers.toResponseDTO(registration.getEvent()),
                "Successfully registered",
                registration.getRegisteredAt()
        );
    }

    public static Registration toEntity(
            Date registrationAt,
            Event event,
            AppUser user
    ) {
        Registration entity = new Registration();
        entity.setEvent(event);
        entity.setUser(user);
        entity.setRegisteredAt(registrationAt);
        return entity;
    }
}
