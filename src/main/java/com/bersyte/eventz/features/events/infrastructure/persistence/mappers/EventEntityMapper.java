package com.bersyte.eventz.features.events.infrastructure.persistence.mappers;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EventEntityMapper {
    
    private final UserEntityMapper userEntityMapper;
    
    public PagedResult<Event> toPagedResult(Page<EventEntity> eventEntityPage){
        List<Event> eventList = eventEntityPage.getContent().stream().map(this::toDomain).toList();
        return new PagedResult<>(
                eventList, eventEntityPage.getTotalElements(),
                eventEntityPage.getTotalPages(),
                eventEntityPage.isLast()
        );
    }
    
    public EventEntityMapper(UserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }
    
    public Event toDomain(EventEntity entity) {
        if (entity == null) {
            throw new NullPointerException ("Entity cannot be null");
        }
        final AppUser organizer = userEntityMapper.toDomain(entity.getOrganizer());
  
        
        return Event.restore(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
               entity.getMaxParticipants(),
               entity.getCreatedAt(),
               entity.getUpdateAt(), organizer,
               entity.getParticipantsCount()
       );
   }

    public EventEntity toEventEntity(Event event) {
        if (event == null) {
            throw new NullPointerException ("Request data cannot be null");
        }
      
        UserEntity organizer = userEntityMapper.toUserEntity(event.getOrganizer());
        EventEntity entity = new EventEntity();
        entity.setTitle(event.getTitle());
        entity.setDescription(event.getDescription());
        entity.setLocation(event.getLocation());
        entity.setDate(event.getDate());
        entity.setOrganizer(organizer);
        entity.setMaxParticipants(event.getMaxParticipants());
        return  entity;
    }
}
