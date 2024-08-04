package com.bersyte.eventz.services;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.mapper.AppMapper;
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
            AppMapper.toResponseDTO(event)
        ).toList();
    }

    public EventResponseDTO getEventById(Integer id){
        Event event = this.findEventById(id);
        return AppMapper.toResponseDTO(event);
    }

    public Event updateEvent(Integer id, EventRequestDTO data){
         Event event = this.findEventById(id);

         //TODO - Improve this validation - find the best way to do this
         if(data.title() != null ){
           event.setTitle(data.title());
         }
         if(data.description() != null ){
            event.setDescription(data.description());
         }
         if(data.location() != null ){
           event.setLocation(data.location());
         }
         if(data.date() != null ){
           event.setDate(new Date(data.date()));
         }

         try {
            repository.save(event);
         } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getLocalizedMessage());
         }
         return event;
    }


    public void deleteEvent(Integer id){
        Event eventOptional = this.findEventById(id);
        repository.delete(eventOptional);
    }

    private Event findEventById(Integer id){
        Optional<Event> eventOptional = repository.findById(id);

        if(!eventOptional.isPresent()){
            throw new IllegalArgumentException("Event not found");
        }
       return eventOptional.get();
    }

    public List<EventResponseDTO> filterEvents(
        int page, 
        int size,
        String title, 
        String location
    ){

        title = (title != null) ? title : " ";
        location = (location != null) ? location : " ";

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPerPage = this.repository.findFilteredEvents(
            title, location, pageable
        );
        return eventsPerPage.stream().map(event ->  
            AppMapper.toResponseDTO(event)
        ).toList();
    }
}
