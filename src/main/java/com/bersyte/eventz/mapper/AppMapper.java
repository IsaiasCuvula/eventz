package com.bersyte.eventz.mapper;
import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.models.Event;
import java.util.Date;


public class AppMapper {

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
