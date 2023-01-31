package com.example.ecommerce.dto.order;

import com.example.ecommerce.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDto {

    private ProductDto product;
    private int amount;

}
