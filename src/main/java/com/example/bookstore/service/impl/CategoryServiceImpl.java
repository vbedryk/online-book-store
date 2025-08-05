package com.example.bookstore.service.impl;

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.dto.user.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import com.example.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.getReferenceById(id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
        Category savedCategory = categoryRepository.save(categoryMapper
                .toModel(categoryRequestDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(CategoryRequestDto categoryRequestDto, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category by id "
                        + id + " doesn't exist"));
        categoryMapper.updateCategoryFromDB(categoryRequestDto, category);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }
}
