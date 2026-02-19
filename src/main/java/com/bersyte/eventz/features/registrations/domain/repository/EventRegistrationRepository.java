package com.bersyte.eventz.features.registrations.domain.repository;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRegistrationRepository {
    EventRegistration joinEvent(EventRegistration registration);
    EventRegistration update(EventRegistration registration);
    Optional<EventRegistration> findById(UUID id);
    boolean alreadyRegistered(UUID eventId, UUID userId,  List<RegistrationStatus> statuses);
    Optional<EventRegistration> findUserRegistrationByStatus(UUID eventId, UUID userId, RegistrationStatus status);
    PagedResult<EventRegistration> fetchParticipants(UUID eventId, Pagination pagination);
    Optional<EventRegistration> findRegistrationByCheckInToken(String checkInToken);
}
