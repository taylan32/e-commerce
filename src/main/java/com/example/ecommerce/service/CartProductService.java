package com.example.ecommerce.service;

import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.repository.CartProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartProductService {

    private final CartProductRepository cartProductRepository;

    public CartProductService(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    protected CartProduct save(CartProduct cartProduct) {
        return cartProductRepository.save(cartProduct);
    }
    protected void incrementAmount(Long id) {
        CartProduct data = cartProductRepository.findById(id).orElseThrow(() -> BaseException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage("Requested product not found")
                .build());
        int amount = data.getAmount();
        data.setAmount(amount + 1);
        cartProductRepository.save(data);
    }

    protected void removeItemFromCart(Long productId, Long cartId) {
        cartProductRepository.delete(cartProductRepository.getByProductIdAndCartId(productId, cartId));
    }

}
