package com.bersyte.eventz.events;
import java.util.Date;


public class EventMappers {

   public static EventResponseDTO toResponseDTO(Event entity){
       return new EventResponseDTO(
               entity.getId(),
               entity.getTitle(),
               entity.getDescription(),
               entity.getLocation(),
               entity.getDate(),
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
