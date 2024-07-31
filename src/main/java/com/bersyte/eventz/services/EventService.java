package com.bersyte.eventz.services;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.models.Event;
import com.bersyte.eventz.repositories.EventRepository;

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

    public List<EventResponseDTO> getAllEvents(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPerPage = repository.findAll(pageable);
        
        return eventsPerPage.stream().map(event ->  
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

    public EventResponseDTO getEventById(Integer id){
        Optional<Event> eventOptional = repository.findById(id);

        if(!eventOptional.isPresent()){
            throw new IllegalArgumentException("Event not found");
        }

        Event event = eventOptional.get();

        return new EventResponseDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getLocation(),
            event.getDate(),
            event. getCreatedAt()
        );

    }
}
