package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.category.CategoryDto;
import com.example.ecommerce.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoConverter {

    public CategoryDto convert(Category from) {
        return new CategoryDto(
                from.getId(),
                from.getCategoryName()
        );
    }

}
