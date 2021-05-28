package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.security.JwtHelper;
import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.mapper.CategoryMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.Event;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.CategoryRepository;
import com.ngteam.toastapp.repositories.EventRepository;
import com.ngteam.toastapp.services.CategoryService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImpl extends ResponseCreator implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final JwtHelper jwtHelper;
    private final CategoryMapper categoryMapper;

    @Override
    public ResponseEntity createCategory(String authorization, CategoryDto categoryDto) {
        User user = jwtHelper.getUserFromHeader(authorization);
        String categoryNameDto = categoryDto.getName();
        String convertedCategoryName = categoryNameDto.substring(0, 1).toUpperCase() + categoryNameDto.substring(1).toLowerCase();
        Optional<Category> optionalUserCategory = categoryRepository.findByNameAndUserId(convertedCategoryName, user);
        if (optionalUserCategory.isPresent()) { return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED); }
        Category category = Category.builder()
                .name(convertedCategoryName)
                .user(user)
                .build();
        categoryRepository.save(category);
        return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
    }

    public ResponseEntity updateCategoryById(String authorization, CategoryDto categoryDto, long id) {
        User user = jwtHelper.getUserFromHeader(authorization);
        Long userId = user.getId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        Optional<Category> optionalCategory = categoryRepository.findByNameAndUserId(categoryDto.getName(), user);
        if (optionalCategory.isPresent()) { return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED); }
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
    }

    @Override
    public ResponseEntity getAllCategories(String authorization) {
        User user = jwtHelper.getUserFromHeader(authorization);
        List<Category> categories = categoryRepository.findAllByUserId(user.getId());
        categories.addAll(categoryRepository.findDefaultCategories());
        return createGoodResponse(categoryMapper.toCategoryDtoList(categories));
    }

    @Override
    public ResponseEntity deleteCategoryById(String authorization, long id) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        List<Event> events = eventRepository.findAllByCategory(category);
        for(Event event: events) {
            event.setCategory(categoryRepository.findById(0L).
                    orElseThrow(() -> new NotFoundException("Сперва необходимо создать User(Admin) c CategoryId = 0")));
            eventRepository.save(event);
        }
        categoryRepository.delete(category);
        return createGoodResponse("Deleted");
    }

    @Override
    public ResponseEntity getCategoryById(String authorization, long id) {
        Long userId = jwtHelper.getUserFromHeader(authorization).getId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        return createGoodResponse(categoryMapper.toCategoryOutDtoConvert(category));
    }

    @Override
    public void createDefaultCategories() {
        String [] categoryNames = {"Друзья", "Родственники", "Коллеги", "Животные"};
        for (int i = 0; i < categoryNames.length; i++) {
            Optional<Category> category0 = categoryRepository.findByName(categoryNames[i]).stream().findFirst();
            if (category0.isPresent()) {
                continue;
            }
            Category category = Category.builder()
                    .name(categoryNames[i])
                    .build();
            categoryRepository.save(category);
        }
    }
}
