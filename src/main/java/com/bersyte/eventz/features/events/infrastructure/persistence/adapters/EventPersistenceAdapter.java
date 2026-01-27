package com.bersyte.eventz.features.events.infrastructure.persistence.adapters;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.domain.repository.EventRepository;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import com.bersyte.eventz.features.events.infrastructure.persistence.repositories.EventJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class EventPersistenceAdapter implements EventRepository {
    private final EventJpaRepository eventJpaRepository;
    private final EventEntityMapper entityMapper;
    
    public EventPersistenceAdapter(EventJpaRepository eventJpaRepository , EventEntityMapper entityMapper) {
        this.eventJpaRepository = eventJpaRepository;
        this.entityMapper = entityMapper;
    }
    
    @Override
    public Event createEvent(Event event) {
        EventEntity entity = entityMapper.toEventEntity(event);
        EventEntity savedEvent = eventJpaRepository.save(entity);
        return entityMapper.toDomain(savedEvent);
    }
    
    @Override
    public Event updateEvent(Event event) {
        EventEntity entity = entityMapper.toEventEntity(event);
        EventEntity updatedEvent = eventJpaRepository.save(entity);
        return entityMapper.toDomain(updatedEvent);
    }
    
    @Override
    public void deleteEvent(String id) {
        eventJpaRepository.deleteById(id);
    }
    
    @Override
    public Optional<Event> findEventById(String id) {
        return eventJpaRepository.findById(id).map(
                entityMapper::toDomain
        );
    }
    
    @Override
    public PagedResult<Event> fetchEventsByOrganizer(String organizerId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<EventEntity> result = eventJpaRepository.fetchEventsByOrganizer(organizerId, pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public PagedResult<Event> fetchEvents(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<EventEntity> result = eventJpaRepository.findAll(pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public PagedResult<Event> fetchEventsByDate(Pagination pagination, LocalDateTime start, LocalDateTime end) {
         Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<EventEntity> result = eventJpaRepository.findByDateBetween(start, end, pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public PagedResult<Event> fetchUpcomingEvents(LocalDateTime now,Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<EventEntity> result = eventJpaRepository.findUpcomingEvents(now, pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public PagedResult<Event> filterEvents(String title, String location, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        String cleanTitle = this.sanitize(title);
        String cleanLocation =  this.sanitize(location);
        Page<EventEntity> result = eventJpaRepository.filterEvents(cleanTitle, cleanLocation, pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public void incrementParticipantCount(String eventId) {
        eventJpaRepository.incrementParticipantCount(eventId);
    }
    
    @Override
    public void decrementParticipantCount(String eventId) {
        eventJpaRepository.decrementParticipantCount(eventId);
    }
    
    private String sanitize(String input) {
        if (input == null || input.isBlank()) return null;
        return input.trim();
    }
}
