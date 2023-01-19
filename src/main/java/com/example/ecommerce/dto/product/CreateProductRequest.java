package com.example.ecommerce.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Product name is not allowed to be empty")
    @Size(min = 2, max = 30, message = "Prodcut name should be contain between 2 and 30 characters")
    private String productName;

    @NotNull
    @Min(value = 0, message = "Price should be greater than 0.")
    private BigDecimal price;

    @Min(value = 0, message = "Stock amount should be greater than 0.")
    private int unitsInStock;
    private String definition;

    @NotNull
    private Long categoryId;

}
