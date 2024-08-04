package com.bersyte.eventz.mapper;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.models.Event;

public class AppMapper {

    //TODO-Using a Mapping Library: Consider using a library like MapStruct, ModelMapper, or Orika

    public static EventResponseDTO toResponseDTO(Event event){
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getCreatedAt()
            );
    }
    
}
