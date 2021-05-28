package com.ngteam.toastapp.repositories;

import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.name = ?1 and c.user = ?2")
    Optional<Category> findByNameAndUserId (String name, User user);
    List<Category> findAllByUserId(long id);
    @Query("select c from Category c where c.user is null")
    List<Category> findDefaultCategories();
    @Query("select c from Category c where c.name = ?1")
    List<Category> findByName(String name);
    Optional<Category> findByIdAndUserId(long id, Long userId);
}