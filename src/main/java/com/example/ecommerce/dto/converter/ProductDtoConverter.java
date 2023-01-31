package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter {

    private final CategoryDtoConverter categoryDtoConverter;

    public ProductDtoConverter(CategoryDtoConverter categoryDtoConverter) {
        this.categoryDtoConverter = categoryDtoConverter;
    }

    public ProductDto convert(Product from) {
        return new ProductDto(
                from.getId(),
                from.getProductName(),
                from.getPrice(),
                from.getUnitsInStock(),
                from.getDefinition(),
                categoryDtoConverter.convert(from.getCategory())
        );
    }

}
