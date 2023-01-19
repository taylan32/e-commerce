package com.example.ecommerce.service.filter;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    @Nullable
    private String productName;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Integer stockFrom;
    private Integer stockTo;
    private Long categoryId;
}
