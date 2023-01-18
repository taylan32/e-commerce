package com.example.ecommerce.service;

import com.example.ecommerce.converter.CategoryDtoConverter;
import com.example.ecommerce.dto.category.CategoryDto;
import com.example.ecommerce.dto.category.CreateCategoryRequest;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoConverter converter;

    public CategoryService(CategoryRepository categoryRepository, CategoryDtoConverter converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    public CategoryDto createCategory(CreateCategoryRequest request) {

        checkIfCategoryNameExists(request.getCategoryName().strip());

        return converter.convert(categoryRepository.save(new Category(null, request.getCategoryName())));

    }

    public CategoryDto getCategoryById(Long id) {
        return converter.convert(findCategoryById(id));
    }

    protected Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> BaseException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage("Requested category not found")
                .build());

    }

    public List<CategoryDto> getAllCategory() {
        return categoryRepository.findAll()
                .stream().
                map(converter::convert)
                .collect(Collectors.toList());
    }

    private void checkIfCategoryNameExists(String categoryName) {
        if(categoryRepository.existsByCategoryName(categoryName)) {
            throw BaseException.builder()
                    .errorMessage("Category name already exists")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

}
