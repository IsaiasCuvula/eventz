package com.bersyte.eventz.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bersyte.eventz.models.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{}
