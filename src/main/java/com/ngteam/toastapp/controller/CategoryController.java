package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.services.impl.CategoryServiceImpl;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/category")
@AllArgsConstructor
public class CategoryController extends ResponseCreator {

    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping
    ResponseEntity createCategory(@RequestHeader String authorization, @RequestBody CategoryDto categoryDto) {
        return categoryServiceImpl.createCategory(authorization, categoryDto);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateCategoryById(@RequestBody CategoryDto categoryDto, @PathVariable long id) {
        return categoryServiceImpl.updateCategory(categoryDto, id);
    }

    @GetMapping
    ResponseEntity getAllCategories() {
        return categoryServiceImpl.getAllCategories();
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getCategoryById(@PathVariable long id) {
        return categoryServiceImpl.getCategoryById(id);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteCategoryById(@PathVariable long id) {
        return categoryServiceImpl.deleteCategoryById(id);
    }
}