package com.bersyte.eventz.event_registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

  @Query(
          "select r from Registration  r " +
                  "where r.user.id = :userId and r.event.id = :eventId"
  )
  Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

  @Modifying
  @Transactional
  @Query(
          "update Registration r set r.status = :status, r.updateAt = :updateAt " +
                  "where r.user.id = :userId and r.event.id = :eventId"
  )
  void updateRegistrationStatus(
          Long userId,
          Long eventId,
          RegistrationStatus status,
          Date updateAt
  );
}
