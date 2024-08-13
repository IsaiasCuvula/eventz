package com.bersyte.eventz.controllers;

import com.bersyte.eventz.dto.EventRequestDTO;
import com.bersyte.eventz.dto.EventResponseDTO;
import com.bersyte.eventz.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/eventz")
public class EventController {

   private final EventService service;


   @PostMapping
   public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO data) {
       EventResponseDTO responseDTO = this.service.createEvent(data);
       return ResponseEntity.ok(responseDTO);
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
   public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Integer id, @Valid @RequestBody EventRequestDTO data) {
       EventResponseDTO responseDTO = this.service.updateEvent(id, data);
       return ResponseEntity.ok(responseDTO);
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

    @GetMapping("/date")
    public ResponseEntity<List<EventResponseDTO>> getEventsByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long eventDate
    ) {
        List<EventResponseDTO> allEvents = this.service.getEventsByDate(page, size, eventDate);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<EventResponseDTO> upcomingEvents = this.service.getUpcomingEvents(page, size);
        return ResponseEntity.ok(upcomingEvents);
    }
}
