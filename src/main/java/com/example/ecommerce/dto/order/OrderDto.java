package com.example.ecommerce.dto.order;

import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String orderDate;
    private OrderStatus orderStatus;
    private UserDto user;
    private List<ProductDto> products;
    private BigDecimal cost;

}
