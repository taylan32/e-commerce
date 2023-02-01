package com.example.ecommerce.service;

import com.example.ecommerce.dto.cart.CartDto;
import com.example.ecommerce.dto.converter.CartDtoConverter;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartDtoConverter converter;
    private final ProductService productService;
    private final CartProductService cartProductService;
    public CartService(CartRepository cartRepository,
                       UserService userService,
                       CartDtoConverter converter,
                       ProductService productService,
                       CartProductService cartProductService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.converter = converter;
        this.productService = productService;
        this.cartProductService = cartProductService;
    }
    public CartDto getCart() {
        return converter.convert(findCartByUserId());
    }

    protected Cart findCartByUserId() {
        return this.cartRepository.getByUser_id(userService.findUserInContext().getId())
                .orElseThrow(() -> BaseException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage("Requested cart not found")
                .build());
    }

    @Transactional
    public void addToCart(Long productId) {
        Product product = productService.findProductById(productId);
        Cart cart = findCartByUserId();
        checkIfUserIsCartOwner(cart);
        if(checkIfProductExistsInsideCart(product, cart)) {
            // increment amount
            List<CartProduct> data = cart.getProducts().stream()
                    .filter(item -> item.getProduct().getId() == productId)
                    .collect(Collectors.toList());
            cartProductService.incrementAmount(data.get(0).getId());
        } else {
            CartProduct cartProduct = cartProductService.save(new CartProduct(null, product, cart, 1));
           cart.getProducts().add(cartProduct);
        }
    }

    public void removeProductFromCart(Long productId) {
        Product product = productService.findProductById(productId);
        Cart cart = findCartByUserId();

        checkIfUserIsCartOwner(cart);
        cartProductService.removeItemFromCart(productId, cart.getId());
    }

    @Transactional
    public void clearCart() {
        Cart cart = findCartByUserId();
        checkIfUserIsCartOwner(cart);
        cart.getProducts().clear();
        cartRepository.save(cart);
    }

    private void checkIfUserIsCartOwner(Cart cart) {
        User user = userService.findUserInContext();

        if(!(cart.getUser().getId().equals(user.getId()))) {
            throw BaseException.builder()
                    .errorMessage("You are not allowed to perform this operation.")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();
        }

    }

    private boolean checkIfProductExistsInsideCart(Product product, Cart cart) {
        List<?> data = cart.getProducts()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .collect(Collectors.toList());

        if(data.isEmpty()) {
            return false;
        }
        return true;
    }

}
