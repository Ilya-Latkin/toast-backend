package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.config.filter.JwtHelper;
import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.mapper.CategoryMapper;
import com.ngteam.toastapp.exceptions.NotFoundException;
import com.ngteam.toastapp.model.Category;
import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.CategoryRepository;
import com.ngteam.toastapp.repositories.UserRepository;
import com.ngteam.toastapp.services.CategoryService;
import com.ngteam.toastapp.utils.ErrorEntity;
import com.ngteam.toastapp.utils.ResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImpl extends ResponseCreator implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtHelper jwtHelper;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity createCategory(String authorization, CategoryDto categoryDto) {
        String token = JwtHelper.getTokenFromHeader(authorization);
        String emailFromToken = jwtHelper.getEmailFromToken(token);
        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new NotFoundException("autizm")); // <--- autizm / shobi ne rugalsa
        String categoryNameDto = categoryDto.getName();
        String convertedCategoryName = categoryNameDto.substring(0, 1).toUpperCase() + categoryNameDto.substring(1).toLowerCase();
        Optional<Category> optionalUserCategory = categoryRepository.findByNameAndUserId(convertedCategoryName, user);
        if (optionalUserCategory.isPresent()) {
            return createErrorResponse(ErrorEntity.CATEGORY_ALREADY_CREATED);
        }
        Category category = new Category(convertedCategoryName, user);
        categoryRepository.save(category);
        return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
    }

    @Override
    public ResponseEntity updateCategory(CategoryDto categoryDto, long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return createGoodResponse(categoryMapper.toCategoryDtoConvert(category));
    }

    @Override
    public ResponseEntity getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return createGoodResponse(categoryMapper.toCategoryDtoList(categories));    }

    @Override
    public ResponseEntity deleteCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(category);
        return createGoodResponse("Deleted");
    }

    @Override
    public ResponseEntity getCategoryById(long id) {
        return createGoodResponse(categoryMapper.toCategoryOutDtoConvert(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"))));

    }
}
