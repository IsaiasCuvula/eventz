package com.bersyte.eventz.features.registrations.domain.repository;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository {
    EventRegistration joinEvent(EventRegistration registration);
    EventRegistration update(EventRegistration registration);
    Optional<EventRegistration> findById(String id);
    boolean alreadyRegistered(String eventId, String userId,  List<RegistrationStatus> statuses);
    Optional<EventRegistration> findUserRegistrationByStatus(String eventId, String userId, RegistrationStatus status);
    PagedResult<EventRegistration> fetchParticipants(String eventId, Pagination pagination);
    Optional<EventRegistration> findRegistrationByCheckInToken(String checkInToken);
}
