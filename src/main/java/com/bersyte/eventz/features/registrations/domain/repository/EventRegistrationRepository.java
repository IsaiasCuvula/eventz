package com.bersyte.eventz.features.registrations.domain.repository;

import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;

import java.util.Optional;

public interface EventRegistrationRepository {
    void removeParticipant(String registerId, String userId);
    EventRegistration joinEvent(EventRegistration registration);
    EventRegistration update(EventRegistration registration);
    EventRegistration delete(EventRegistration registration);
    Optional<EventRegistration> findById(String id);
    Optional<EventRegistration> findByEventId(String eventId);
    Optional<EventRegistration> findByUserId(String userId);
}
