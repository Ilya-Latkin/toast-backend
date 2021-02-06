package com.ngteam.toastapp.services;

import com.ngteam.toastapp.dto.in.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity createCategory(String authorization, CategoryDto categoryDto);
    ResponseEntity updateCategory(CategoryDto categoryDto, long id);
    ResponseEntity getAllCategories();
    ResponseEntity deleteCategoryById(long id);
    ResponseEntity getCategoryById(long id);
}