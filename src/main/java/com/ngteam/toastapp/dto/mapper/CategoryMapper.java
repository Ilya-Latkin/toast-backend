package com.ngteam.toastapp.dto.mapper;

import com.ngteam.toastapp.dto.in.CategoryDto;
import com.ngteam.toastapp.dto.out.CategoryOutDto;
import com.ngteam.toastapp.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Autowired
    UserMapper userMapper;

    public Category toCategoryConvert(CategoryDto categoryDto) {
        return toCategoryConvert(categoryDto, new Category());
    }

    public Category toCategoryConvert(CategoryDto categoryDto, Category category) {
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public CategoryOutDto toCategoryOutDtoConvert(Category category) {
        CategoryOutDto categoryOutDto = new CategoryOutDto();
        categoryOutDto.setId(category.getId());
        categoryOutDto.setName(category.getName());
        categoryOutDto.setEvents(
                category.getEvents()
                        .stream()
                        .map(event -> event.getName() + "#" + event.getId())
                        .collect(Collectors.toList())
        );
        return categoryOutDto;

    }

    public CategoryDto toCategoryDtoConvert(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<Category> toCategoryList(List<CategoryDto> categoryDtos) {
        return categoryDtos
                .stream()
                .map(this::toCategoryConvert)
                .collect(Collectors.toList());
    }

    public List<CategoryOutDto> toCategoryDtoList(List<Category> categories) {
        return categories
                .stream()
                .map(this::toCategoryOutDtoConvert)
                .collect(Collectors.toList());
    }
}
