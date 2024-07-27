package com.beryste.eventz.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.beryste.eventz.models.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{}
