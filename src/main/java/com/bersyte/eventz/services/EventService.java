package com.bersyte.eventz.services;

import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.mapper.EventMappers;
import com.bersyte.eventz.models.Event;
import com.bersyte.eventz.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;


    public EventResponseDTO createEvent(EventRequestDTO data) {
        Event newEvent = EventMappers.toEventEntity(data);
        try {
           repository.save(newEvent);
        } catch (Exception e) {
            String errorMsg = "Something went wrong: " + e.getLocalizedMessage();
            throw new IllegalArgumentException(errorMsg);
        }
        return EventMappers.toResponseDTO(newEvent);
    }

    public List<EventResponseDTO> getAllEvents(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPerPage = repository.findAll(pageable);
        return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
    }

    public EventResponseDTO getEventById(Integer id){
       Event event = this.findEventById(id);
       return EventMappers.toResponseDTO(event);
    }

    public EventResponseDTO updateEvent(Integer id, EventRequestDTO data) {
         Event event = this.findEventById(id);

        try {
            if (data.title() != null) {
                event.setTitle(data.title());
            }
            if (data.description() != null) {
                event.setDescription(data.description());
            }
            if (data.location() != null) {
                event.setLocation(data.location());
            }
            if (data.date() != null) {
                event.setDate(new Date(data.date()));
            }

            repository.save(event);
            //
         } catch (Exception e) {
            String errorMsg = "Something went wrong: " + e.getLocalizedMessage();
            throw new IllegalArgumentException(errorMsg);
         }
        return EventMappers.toResponseDTO(event);
    }


    public void deleteEvent(Integer id){
        Event eventOptional = this.findEventById(id);
        repository.delete(eventOptional);
    }

    private Event findEventById(Integer id){
        Optional<Event> eventOptional = repository.findById(id);

        if(eventOptional.isEmpty()){
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
        return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
    }

    public List<EventResponseDTO> getEventsByDate(int page, int size, Long date) {

        Pageable pageable = PageRequest.of(page, size);

        Date dateEvent = (date != null) ? new Date(date) : new Date();
        Page<Event> eventsPerPage = this.repository.getEventsByDate(dateEvent, pageable);
        return eventsPerPage.stream().map(EventMappers::toResponseDTO).toList();
    }


}
