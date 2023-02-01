package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    @Query(value = "SELECT * FROM cart_products x where x.product_id=:productId AND x.cart_id=:cartId", nativeQuery = true)
    CartProduct getByProductIdAndCartId(Long productId, Long cartId);

}
