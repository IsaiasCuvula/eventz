package com.bersyte.eventz.features.events.infrastructure.persistence.repositories;

import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {
  
  @Modifying
  @Query("UPDATE EventEntity e SET e.participantsCount = e.participantsCount + 1" +
                 " WHERE e.id = :id AND e.participantsCount < e.maxParticipants")
  int incrementParticipantCount(@Param("id") UUID id);
  
  @Modifying
  @Query("UPDATE EventEntity e SET e.participantsCount = e.participantsCount - 1" +
                 " WHERE e.id = :id AND e.participantsCount > 0")
  int decrementParticipantCount(@Param("id") UUID id);
  
  @Query("SELECT e FROM EventEntity e WHERE (e.organizer.id = :id) order by e.date")
  Page<EventEntity> fetchEventsByOrganizer(@Param("id") UUID id, Pageable pageable);
  
  @Query(
    "SELECT e FROM EventEntity e " +
    "WHERE (:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) ) AND " +
    "(:location  IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')) ) "
  )
  Page<EventEntity> filterEvents(
          @Param("title") String title,
          @Param("location") String location,
          Pageable pageable
  );

  @Query("SELECT e FROM EventEntity e WHERE e.date BETWEEN :start AND :end")
  Page<EventEntity> findByDateBetween(
          @Param("start") LocalDateTime start,
          @Param("end")   LocalDateTime end,
          Pageable pageable
  );

  @Query("SELECT e FROM EventEntity e WHERE e.date >= :now ORDER BY e.date ASC ")
  Page<EventEntity> findUpcomingEvents(@Param("now") LocalDateTime now, Pageable pageable);
}
