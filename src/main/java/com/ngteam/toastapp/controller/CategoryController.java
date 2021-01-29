package com.ngteam.toastapp.controller;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.mapper.CategoryMapper;
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
        if(jwtHelper.validateToken(token)) {
            Optional<Category> categoryOptional = categoryRepository.findByName(categoryDto.getName());
            if(!categoryOptional.isPresent()) {
                String string = categoryDto.getName();
                String convertedString = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
                Category category = new Category(convertedString);
                String emailFromToken = jwtHelper.getEmailFromToken(token);
                Optional<User> optionalUser = userRepository.findByEmail(emailFromToken);
                User user = optionalUser.get();
                category.setUser(user);
                categoryRepository.save(category);
                return createGoodResponse(category);
            } else return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity updateCategoryById(@RequestHeader String authorization, @RequestBody CategoryDto categoryDto, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if(categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                category.setName(categoryDto.getName());
                categoryRepository.save(category);
                return createGoodResponse(category);
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping
    ResponseEntity getAllCategories(@RequestHeader String authorization) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            List<Category> categories = categoryRepository.findAll();
            return createGoodResponse(categoryMapper.toCategoryDtoList(categories));
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity getCategoryById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if(categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                return createGoodResponse(categoryMapper.toCategoryOutDtoConvert(category));
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteCategoryById(@RequestHeader String authorization, @PathVariable long id) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        if(jwtHelper.validateToken(token)) {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if(categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                categoryRepository.delete(category);
                return createGoodResponse("Deleted");
            } else return createErrorResponse(ErrorEntity.NOT_FOUND);
        } else return createErrorResponse(ErrorEntity.INVALID_TOKEN_TOKEN);
    }
}
