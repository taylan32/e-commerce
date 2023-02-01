package com.example.ecommerce.dto.cart;

import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.model.CartProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long id;
    private List<CartProduct> products;
    private BigDecimal price;
    private UserDto user;

}
