package com.bersyte.eventz.features.registrations.infrastructure.persistence.adapters;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers.EventRegistrationEntityMapper;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.repository.EventRegistrationJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventRegistrationPersistenceAdapter implements EventRegistrationRepository {
    private final EventRegistrationEntityMapper entityMapper;
    private final EventRegistrationJpaRepository registrationJpaRepository;
    
    public EventRegistrationPersistenceAdapter(EventRegistrationEntityMapper entityMapper, EventRegistrationJpaRepository registrationJpaRepository) {
        this.entityMapper = entityMapper;
        this.registrationJpaRepository = registrationJpaRepository;
    }
    
    @Override
    public EventRegistration joinEvent(EventRegistration registration) {
        EventRegistrationEntity entity = entityMapper.toEntity(registration);
        EventRegistrationEntity saved = registrationJpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }
    
    @Override
    public EventRegistration update(EventRegistration registration) {
        EventRegistrationEntity entity = entityMapper.toEntity(registration);
        EventRegistrationEntity saved = registrationJpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }
    
    @Override
    public Optional<EventRegistration> findById(String id) {
        return registrationJpaRepository.findById(id).map(
                entityMapper::toDomain
        );
    }
    
    @Override
    public boolean alreadyRegistered(String eventId, String userId, List<RegistrationStatus> statuses) {
        return registrationJpaRepository.alreadyRegistered(
                eventId, userId,statuses
        );
    }
    
    @Override
    public Optional<EventRegistration> findUserRegistrationByStatus(String eventId, String userId, RegistrationStatus status) {
        return registrationJpaRepository.findRegistrationByStatus(eventId, userId, status)
                       .map(entityMapper::toDomain);
    }
    
    @Override
    public PagedResult<EventRegistration> fetchParticipants(String eventId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<EventRegistrationEntity> result = registrationJpaRepository.findAllByEventId(eventId,pageable);
        return entityMapper.toPagedResult(result);
    }
    
    @Override
    public Optional<EventRegistration> findRegistrationByCheckInToken(String checkInToken) {
        return registrationJpaRepository.findByCheckInToken(checkInToken).map(
                entityMapper::toDomain
        );
    }
}
