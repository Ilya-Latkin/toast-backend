package com.ngteam.toastapp.repositories;

import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.Event;
import com.ngteam.toastapp.model.EventType;
import com.ngteam.toastapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e where e.name = ?1 and e.user = ?2")
    Optional<Event> findByNameAndUserId (String name, User user);
    List<Event> findAllByUserId(long id);
    List<Event> findAllByCategory(Category category);
    List<Event> findAllByEventType(EventType eventType);
    Optional<Event> findByIdAndUserId(long id, Long userId);
}