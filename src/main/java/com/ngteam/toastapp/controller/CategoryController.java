package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.services.CategoryService;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/category")
@AllArgsConstructor
public class CategoryController extends ResponseCreator {

    private final CategoryService categoryServiceImpl;

    @PostMapping
    ResponseEntity createCategory(@RequestHeader String authorization, @RequestBody CategoryDto categoryDto) {
        return categoryServiceImpl.createCategory(authorization, categoryDto);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateCategoryById(@RequestHeader String authorization, @RequestBody CategoryDto categoryDto, @PathVariable long id) {
        return categoryServiceImpl.updateCategoryById(authorization, categoryDto, id);
    }

    @GetMapping
    ResponseEntity getAllCategories(@RequestHeader String authorization) {
        return categoryServiceImpl.getAllCategories(authorization);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getCategoryById(@RequestHeader String authorization, @PathVariable long id) {
        return categoryServiceImpl.getCategoryById(authorization, id);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteCategoryById(@RequestHeader String authorization, @PathVariable long id) {
        return categoryServiceImpl.deleteCategoryById(authorization, id);
    }
}