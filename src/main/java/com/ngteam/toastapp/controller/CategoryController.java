package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.mapper.CategoryMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.CategoryRepository;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/category")
@AllArgsConstructor
public class CategoryController extends ResponseCreator {

    private final CategoryRepository categoryRepository;
    private final JwtHelper jwtHelper;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity createCategory(@RequestHeader String authorization, @RequestBody CategoryDto categoryDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryDto.getName());
        if (!categoryOptional.isPresent()) {
            String string = categoryDto.getName();
            String convertedString = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
            Category category = new Category(convertedString);
            String emailFromToken = jwtHelper.getEmailFromToken(token);
            User user = userRepository.findByEmail(emailFromToken)
                    .orElseThrow(() -> new NotFoundException("User with email " + emailFromToken + " not found")); // <--- autizm
            category.setUser(user);
            categoryRepository.save(category);
            return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
        } else return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateCategoryById(@RequestBody CategoryDto categoryDto, @PathVariable long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
    }

    @GetMapping
    ResponseEntity getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return createGoodResponse(categoryMapper.toCategoryDtoList(categories));
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getCategoryById(@PathVariable long id) {
        return createGoodResponse(categoryMapper.toCategoryOutDtoConvert(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"))));
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteCategoryById(@PathVariable long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(category);
        return createGoodResponse("Deleted");
    }
}