package com.example.ecommerce.dto.product;

import com.example.ecommerce.dto.category.CategoryDto;
import com.example.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String productName;
    private BigDecimal price;
    private int unitsInStock;
    private String definition;
    private CategoryDto category;

}
