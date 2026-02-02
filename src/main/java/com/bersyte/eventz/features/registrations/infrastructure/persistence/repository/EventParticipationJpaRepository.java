package com.bersyte.eventz.features.registrations.infrastructure.persistence.repository;

import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventParticipationJpaRepository extends JpaRepository<EventRegistrationEntity, Long> {

  @Query(
          "select evPart from EventRegistrationEntity  evPart " +
                  "where evPart.user.id = :userId and evPart.event.id = :eventId"
  )
  Optional<EventRegistrationEntity> findByUserIdAndEventId(String userId, String eventId);
  
  @Query("SELECT EXISTS (SELECT 1 FROM event_registrations r WHERE r.event.id = :eventId AND r.user.id = :userId AND r.status != 'CANCELLED')")
  boolean alreadyRegistered(@Param("eventId") String eventId, @Param("userId") String userId);
  
  

  
}
