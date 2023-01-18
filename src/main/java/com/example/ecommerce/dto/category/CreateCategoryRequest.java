package com.example.ecommerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is not allowed to be empty")
    @Size(min = 2, max = 20, message = "Category name should contain between 2 and 20 characters.")
    private String categoryName;
}
