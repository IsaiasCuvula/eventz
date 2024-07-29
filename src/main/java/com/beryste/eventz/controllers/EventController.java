package com.beryste.eventz.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.beryste.eventz.dto.EventRequestDTO;
import com.beryste.eventz.models.Event;
import com.beryste.eventz.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/v1/api/eventz")
public class EventController {

   private final EventService service;

   public EventController(EventService service){
    this.service = service;
   }

   @PostMapping
   public ResponseEntity<Event> create(@RequestBody EventRequestDTO data) {
       Event event = this.service.createEvent(data);
       return ResponseEntity.ok(event);
   }
   
}
