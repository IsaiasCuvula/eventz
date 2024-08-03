package com.bersyte.eventz.repositories;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bersyte.eventz.models.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{

  //Event - entity name
  @Query(
    "SELECT e FROM Event e " +  
    "WHERE (:title = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) ) AND " +  
    "(:location = '' OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')) ) "
  )
  public Page<Event> findFilteredEvents(
    @Param("title") String title, 
    @Param("location") String location, 
    Pageable pageable
  );
}
