package com.bersyte.eventz.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface EventRepository extends JpaRepository<Event, Long> {

  //Event - entity name
  @Query(
    "SELECT e FROM Event e " +  
    "WHERE (:title = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) ) AND " +  
    "(:location = '' OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')) ) "
  )
  Page<Event> findFilteredEvents(
          @Param("title") String title,
          @Param("location") String location,
          Pageable pageable
  );

    @Query("SELECT e FROM Event e WHERE (e.date = :date ) ")
    Page<Event> getEventsByDate(@Param("date") Date date, Pageable pageable);

  @Query("SELECT e FROM Event e WHERE e.date >= current_date ")
  Page<Event> getUpcomingEvents(@Param("date") Date date, Pageable pageable);
}
