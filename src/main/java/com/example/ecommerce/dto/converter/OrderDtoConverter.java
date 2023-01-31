package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.order.OrderDto;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDtoConverter {
    private final UserDtoConverter userDtoConverter;
    private final ProductDtoConverter productDtoConverter;


    public OrderDtoConverter(UserDtoConverter userDtoConverter,
                             ProductDtoConverter productDtoConverter ) {
        this.userDtoConverter = userDtoConverter;
        this.productDtoConverter = productDtoConverter;

    }

    public OrderDto convert(Order from) {
        return new OrderDto(
                from.getId(),
                from.getOrderDate().toString(),
                from.getOrderStatus(),
                userDtoConverter.convert(from.getUser()),
                from.getProducts()
                        .stream()
                        .map(productDtoConverter::convert)
                        .collect(Collectors.toList()),
                new BigDecimal(computeTotalPrice(from.getProducts()))
        );
    }

    private int computeTotalPrice(List<Product> products) {

        List<Integer> prices = new ArrayList<>();
        products.forEach(item -> {
            prices.add(Integer.valueOf(item.getPrice().intValue()));
        });

        return prices.stream().mapToInt(Integer::intValue).sum();

    }

}
