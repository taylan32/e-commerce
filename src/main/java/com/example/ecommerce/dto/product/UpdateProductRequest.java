package com.example.ecommerce.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    private Long id;
    @Size(min = 2, max = 30, message = "Product name should be contain between 2 and 30 characters")
    private String productName;

    @Min(value = 0, message = "Stock amount should be greater than 0.")
    private BigDecimal price;

    @Min(value = 0, message = "Stock amount should be greater than 0.")
    private Integer unitsInStock;

    private String definition;

}
