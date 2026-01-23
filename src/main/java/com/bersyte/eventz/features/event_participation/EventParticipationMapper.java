package com.bersyte.eventz.features.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.features.events.EventEntity;
import com.bersyte.eventz.features.events.EventMappers;
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
            EventEntity event,
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
