package com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EventRegistrationEntityMapper {

    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public EventRegistrationEntityMapper(EventEntityMapper eventEntityMapper, UserEntityMapper userEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }
    
    
    public PagedResult<EventRegistration> toPagedResult(Page<EventRegistrationEntity> entityPage){
        List<EventRegistration> registrationList = entityPage.getContent().stream().map(this::toDomain).toList();
        return new PagedResult<>(
                registrationList, entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.isLast()
        );
    }

    public EventRegistration toDomain(EventRegistrationEntity entity) {
        Event domainEvent = eventEntityMapper.toDomain(entity.getEvent());
        AppUser domainUser = userEntityMapper.toDomain(entity.getUser());
        AppUser domainCheckedInBy = userEntityMapper.toDomain(entity.getCheckedInBy());
        
        return EventRegistration.restore(
                entity.getId(),
                entity.getStatus(),
                entity.getCheckInToken(),
                domainEvent,
                domainUser,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                domainCheckedInBy,
                entity.getCheckedInAt()
        );
    }

    public EventRegistrationEntity toEntity(EventRegistration domain) {
        EventRegistrationEntity entity = new EventRegistrationEntity();
        
        EventEntity event = eventEntityMapper.toEventEntity(domain.getEvent());
        UserEntity user = userEntityMapper.toUserEntity(domain.getUser());
        UserEntity checkedInBy = userEntityMapper.toUserEntity(domain.getCheckedInBy());
        
        entity.setId(domain.getId());
        entity.setStatus(domain.getStatus());
        entity.setEvent(event);
        entity.setUser(user);
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCheckedInAt(domain.getCheckedInAt());
        entity.setCheckInToken(domain.getCheckInToken());
        entity.setUser(checkedInBy);
        return entity;
    }
}
