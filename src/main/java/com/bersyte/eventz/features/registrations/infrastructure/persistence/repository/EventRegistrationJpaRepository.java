package com.bersyte.eventz.features.registrations.infrastructure.persistence.repository;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationJpaRepository extends JpaRepository<EventRegistrationEntity, String> {

  @Query(
          "select evReg from EventRegistrationEntity  evReg " +
          "where evReg.user.id = :userId and evReg.event.id = :eventId"
  )
  Optional<EventRegistrationEntity> findByUserIdAndEventId(String userId, String eventId);
  
  @Query("SELECT EXISTS (SELECT 1 FROM EventRegistrationEntity r WHERE r.event.id = :eventId " +
                 "AND r.user.id = :userId AND r.status IN :statuses)")
  boolean alreadyRegistered(
          @Param("eventId") String eventId,
          @Param("userId") String userId,
          @Param("statuses")List<RegistrationStatus> statuses
          );
  
  
  @Query("SELECT evReg FROM EventRegistrationEntity evReg WHERE evReg.event.id = :eventId AND evReg.user.id = :userId AND evReg.status != :status")
  Optional<EventRegistrationEntity> findRegistrationByStatus(@Param("eventId") String eventId, @Param("userId") String userId, @Param("status") RegistrationStatus status);
  
  @Query("SELECT evReg FROM EventRegistrationEntity  evReg WHERE evReg.checkInToken = :checkInToken")
  Optional<EventRegistrationEntity> findByCheckInToken(@Param("checkInToken") String checkInToken);
  
  
  @Query("SELECT evReg FROM EventRegistrationEntity evReg WHERE evReg.event.id = :eventId")
  Page<EventRegistrationEntity> findAllByEventId(@Param("eventId") String eventId, Pageable pageable);
  
}
