package com.bersyte.eventz.features.registrations.infrastructure.persistence.repository;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface EventParticipationJpaRepository extends JpaRepository<EventRegistrationEntity, Long> {

  @Query(
          "select evPart from EventRegistrationEntity  evPart " +
                  "where evPart.user.id = :userId and evPart.event.id = :eventId"
  )
  Optional<EventRegistrationEntity> findByUserIdAndEventId(Long userId, Long eventId);

  @Modifying
  @Transactional
  @Query(
          "update EventRegistrationEntity evPart set evPart.status = :status, evPart.updateAt = :updateAt " +
                  "where evPart.user.id = :userId and evPart.event.id = :eventId"
  )
  void updateRegistrationStatus(
          Long userId,
          Long eventId,
          RegistrationStatus status,
          Date updateAt
  );
}
