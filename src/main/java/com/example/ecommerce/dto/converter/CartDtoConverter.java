package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.cart.CartDto;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartProduct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Component
public class CartDtoConverter {

    private final ProductDtoConverter productDtoConverter;
    private final UserDtoConverter userDtoConverter;

    public CartDtoConverter(ProductDtoConverter productDtoConverter,
                            UserDtoConverter userDtoConverter) {
        this.productDtoConverter = productDtoConverter;
        this.userDtoConverter = userDtoConverter;
    }

    /*
    public CartDto convert(Cart from) {
        return new CartDto(
                from.getId(),
                from.getProducts()
                        .stream()
                        .map(cartProductDtoConverter::convert)
                        .collect(Collectors.toList()),
                new BigDecimal(computeTotalPrice(from.getProducts()
                        .stream()
                        .map(cartProductDtoConverter::convert)
                        .collect(Collectors.toList()))
                ),
                userDtoConverter.convert(from.getUser())

        );
    }
    */

    public CartDto convert(Cart from) {
        return new CartDto(
                from.getId(),
                from.getProducts(),
                new BigDecimal(computeTotalPrice(from.getProducts())),
                userDtoConverter.convert(from.getUser())

        );
    }

    private int computeTotalPrice(List<CartProduct> products) {
        List<Integer> prices = new ArrayList<>();
        products.forEach(item -> {
            prices.add(Integer.valueOf(item.getAmount() * item.getProduct().getPrice().intValue()));
        });

        return prices.stream().mapToInt(Integer::intValue).sum();
    }

}
