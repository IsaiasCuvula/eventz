package com.bersyte.eventz.features.events.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;

public class DeleteEventUseCase implements VoidUseCase<String> {
    private final EventRepository repository;
    
    public DeleteEventUseCase(EventRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void execute(String id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Event", id);
        }
        repository.deleteEvent(id);
    }
}
