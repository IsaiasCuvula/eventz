package com.beryste.eventz.services;
import java.util.*;
import org.springframework.stereotype.Service;
import com.beryste.eventz.dto.EventRequestDTO;
import com.beryste.eventz.dto.EventResponseDTO;
import com.beryste.eventz.models.Event;
import com.beryste.eventz.repositories.EventRepository;

@Service
public class EventService {

    private final EventRepository repository;


    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(EventRequestDTO data){

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setLocation(data.location());
        newEvent.setDate(new Date(data.date()));
        newEvent.setCreatedAt(new Date(data.createdAt()));

        try {
            repository.save(newEvent);
        } catch (Exception e) {
           System.out.println("Something went wrong: " + e.getLocalizedMessage());
        }
        return newEvent;
    }

    public List<EventResponseDTO> getAllEvents(){
        List<Event> result = repository.findAll();
        
        return result.stream().map(event ->  
            new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event. getCreatedAt()
            )
        ).toList();
    }
}
