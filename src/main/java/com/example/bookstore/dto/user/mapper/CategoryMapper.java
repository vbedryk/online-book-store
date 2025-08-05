package com.example.bookstore.dto.user.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryRequestDto);

    void updateCategoryFromDB(CategoryRequestDto category,
                              @MappingTarget Category entity);
}
