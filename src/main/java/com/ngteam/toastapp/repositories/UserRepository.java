package com.ngteam.toastapp.repositories;

import com.ngteam.toastapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("select u.user from ConfirmationToken u where u.token = ?1")
    Optional<User> findByConfirmToken(String token);
}
