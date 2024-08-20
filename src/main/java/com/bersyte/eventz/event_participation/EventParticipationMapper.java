package com.bersyte.eventz.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventMappers;

import java.util.Date;


public class EventParticipationMapper {

    public static EventParticipationResponseDto toResponseDTO(EventParticipation registration) {
        return new EventParticipationResponseDto(
                registration.getId(),
                EventMappers.toResponseDTO(registration.getEvent()),
                "Successfully registered",
                registration.getRegisteredAt()
        );
    }

    public static EventParticipation toEntity(
            Date registrationAt,
            Date updateAt,
            Event event,
            AppUser user
    ) {
        EventParticipation entity = new EventParticipation();
        entity.setEvent(event);
        entity.setUser(user);
        entity.setRegisteredAt(registrationAt);
        entity.setUpdateAt(updateAt == null ? new Date() : updateAt);
        return entity;
    }
}
