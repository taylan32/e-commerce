package com.example.ecommerce.service;

import com.example.ecommerce.converter.CategoryDtoConverter;
import com.example.ecommerce.dto.category.CategoryDto;
import com.example.ecommerce.dto.category.CreateCategoryRequest;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    private CategoryDtoConverter converter;

    @BeforeEach
    public void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        converter = mock(CategoryDtoConverter.class);
        categoryService = new CategoryService(categoryRepository, converter);
    }


    @Test
    public void testFindCategoryById_whenCategoryIdExists_shouldReturnCategory() {
        Category category = generateCategory();
        Mockito.when(categoryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(category));

        Category result = categoryService.findCategoryById(Long.valueOf(1));

        assertEquals(category, result);

    }

    @Test
    public void testFindCategoryById_whenCategoryIdDoesNotExist_shouldThrowException() {
        Mockito.when(categoryRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());
        assertThrows(BaseException.class, () -> categoryService.findCategoryById(Long.valueOf(0)));
    }

    @Test
    public void testGetCategoryById_whenCategoryIdExists_shouldReturnCategoryDto() {
        Category category = generateCategory();
        CategoryDto categoryDto = new CategoryDto(category.getId(), category.getCategoryName());

        Mockito.when(categoryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(category));
        Mockito.when(converter.convert(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(Long.valueOf(1));

        assertEquals(categoryDto, result);

    }

    @Test
    public void testgetCategoryById_whenCategoryIdDoesNotExist_shouldThrowException() {
        Mockito.when(categoryRepository.findById(Long.valueOf(0))).thenReturn(Optional.empty());

        assertThrows(BaseException.class, () -> categoryService.getCategoryById(Long.valueOf(0)));
        Mockito.verifyNoInteractions(converter);
    }

    @Test
    public void testCreateCategory_shouldReturnCategoryDto() {
        CreateCategoryRequest request = new CreateCategoryRequest("categoryName");
        Category category = new Category(Long.valueOf(1), request.getCategoryName());

        CategoryDto exptected = new CategoryDto(category.getId(), category.getCategoryName());

        Mockito.when(categoryService.createCategory(request)).thenReturn(exptected);

        CategoryDto result = categoryService.createCategory(request);

        assertEquals(exptected, result);


    }


    public Category generateCategory() {
        return new Category(Long.valueOf(1), "category-name");
    }

}
