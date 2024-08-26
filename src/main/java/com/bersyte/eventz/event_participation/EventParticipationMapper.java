package com.bersyte.eventz.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventMappers;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EventParticipationMapper {

    private final EventMappers eventMappers;

    public EventParticipationMapper(EventMappers eventMappers) {
        this.eventMappers = eventMappers;
    }

    public EventParticipationResponseDto toResponseDTO(EventParticipation registration) {
        return new EventParticipationResponseDto(
                registration.getId(),
                eventMappers.toResponseDTO (registration.getEvent ()),
                "Successfully registered",
                registration.getRegisteredAt()
        );
    }

    public EventParticipation toEntity(
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
