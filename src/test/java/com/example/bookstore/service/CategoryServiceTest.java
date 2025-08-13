package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import com.example.bookstore.service.impl.CategoryServiceImpl;
import com.example.bookstore.util.TestUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category with valid response dto")
    public void save_WithValidData_ShouldReturnCategoryResponseDto() {
        // Given
        Category category = TestUtil.createDefaultCategory();
        CategoryResponseDto categoryResponseDto = TestUtil.createDefaultCategoryResponseDto();
        CategoryRequestDto categoryRequestDto = TestUtil.createDefaultCategoryRequestDto();

        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        // When
        CategoryResponseDto actual = categoryService.save(categoryRequestDto);

        // Then
        assertEquals(categoryResponseDto, actual);
    }

    @Test
    @DisplayName("Get category by existed id should return CategoryResponseDto")
    void getById_ExistId_ShouldReturnCategoryResponseDto() {
        long existedId = 1L;
        Category category = TestUtil.createDefaultCategory();
        CategoryResponseDto expectedDto = TestUtil.createDefaultCategoryResponseDto();

        when(categoryRepository.findById(existedId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // When
        CategoryResponseDto actualDto = categoryService.getById(existedId);

        // Then
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Delete category by existed id")
    void deleteById_ExistId_ShouldDeleteCategory() {
        // Given
        long existedId = 1L;
        when(categoryRepository.existsById(existedId)).thenReturn(true);
        // When
        categoryService.deleteById(existedId);
        //Then
        verify(categoryRepository).existsById(existedId);
        verify(categoryRepository).deleteById(existedId);
    }
}
