package com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers;

import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EventRegistrationEntityMapper {

    private final EventEntityMapper eventEntityMapper;

    public EventRegistrationEntityMapper(EventEntityMapper eventEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
    }

    public TicketResponse toResponseDTO(EventRegistrationEntity registration) {
        return new TicketResponse(
                registration.getId(),
                eventEntityMapper.toDomain(registration.getEvent ()),
                "Successfully registered",
                registration.getRegisteredAt()
        );
    }

    public EventRegistrationEntity toEntity(
            Date registrationAt,
            Date updateAt,
            EventEntity event,
            UserEntity user
    ) {
        EventRegistrationEntity entity = new EventRegistrationEntity();
        entity.setEvent(event);
        entity.setUser(user);
        entity.setRegisteredAt(registrationAt);
        entity.setUpdateAt(updateAt == null ? new Date() : updateAt);
        return entity;
    }
}
