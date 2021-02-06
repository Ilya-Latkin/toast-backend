package com.ngteam.toastapp.repositories;

import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.EventType;
import com.ngteam.toastapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    Optional<EventType> findByName(String name);
    @Query("select e from EventType e where e.name = ?1 and e.user = ?2")
    Optional<EventType> findByNameAndUserId (String name, User user);
}