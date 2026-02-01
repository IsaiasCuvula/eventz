package com.bersyte.eventz.features.registrations.domain.repository;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;

import java.util.Optional;

public interface EventRegistrationRepository {
    void removeParticipant(String registerId, String userId);
    EventRegistration joinEvent(EventRegistration registration);
    EventRegistration update(EventRegistration registration);
    EventRegistration delete(EventRegistration registration);
    Optional<EventRegistration> findById(String id);
    boolean alreadyRegistered(String eventId, String userId);
    Optional<EventRegistration> findUserRegistration(String eventId, String userId);
    PagedResult<EventRegistration> fetchParticipants(String eventId, Pagination pagination);
    Optional<EventRegistration> findRegistrationByCheckInToken(String checkInToken);
}
