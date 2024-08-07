package com.bersyte.eventz.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.models.Event;
import com.bersyte.eventz.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.*;
import org.springframework.web.bind.annotation.PutMapping;
import  jakarta.validation.Valid;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/eventz")
public class EventController {

   private final EventService service;


   @PostMapping
   public ResponseEntity<Event> create(@Valid @RequestBody EventRequestDTO data) {
       Event event = this.service.createEvent(data);
       return ResponseEntity.ok(event);
   }

   @GetMapping
   public ResponseEntity<List<EventResponseDTO>> getAllEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
    List<EventResponseDTO> allEvents = this.service.getAllEvents(page, size);
    return ResponseEntity.ok(allEvents);
   }

   @GetMapping("{id}")
   public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Integer id) {
        EventResponseDTO response = this.service.getEventById(id);
       return ResponseEntity.ok(response);
   }

   @PutMapping("{id}")
   public ResponseEntity<Event> updateEvent(@PathVariable Integer id,@Valid @RequestBody EventRequestDTO data) {
        Event event = this.service.updateEvent(id, data);
        return ResponseEntity.ok(event);
   }

   @ResponseStatus(HttpStatus.NO_CONTENT)
   @DeleteMapping("{id}")
   public void deleteEvent(@PathVariable Integer id) {
       this.service.deleteEvent(id);
   }

   @GetMapping("/filter")
   public ResponseEntity<List<EventResponseDTO>> filterEvents(
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size,
     @RequestParam(required = false) String title,
     @RequestParam(required = false) String location
   ) {
     List<EventResponseDTO> allEvents = this.service.filterEvents(page, size, title, location);
     return ResponseEntity.ok(allEvents);
   }
}
