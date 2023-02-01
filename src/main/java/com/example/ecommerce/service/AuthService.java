package com.example.ecommerce.service;

import com.example.ecommerce.dto.auth.LoginRequest;
import com.example.ecommerce.dto.auth.RegisterRequest;
import com.example.ecommerce.dto.auth.TokenResponseDto;
import com.example.ecommerce.dto.converter.TokenResponseDtoConverter;
import com.example.ecommerce.dto.converter.UserDtoConverter;
import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private UserDtoConverter userDtoConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenResponseDtoConverter converter;
    private final CartService cartService;
    private final UserRepository userRepository;


    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       TokenService tokenService,
                       UserDtoConverter userDtoConverter,
                       PasswordEncoder passwordEncoder,
                       TokenResponseDtoConverter converter,
                       CartService cartService,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.userDtoConverter = userDtoConverter;
        this.passwordEncoder = passwordEncoder;
        this.converter = converter;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    public TokenResponseDto login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return converter.convert(
                    tokenService.genereteToken(authentication),
                    userService.findUserByUsername(request.getUsername())
            );
        } catch (final Exception exception) {
            throw  BaseException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .errorMessage("Username or password is incorrect")
                    .build();
        }
    }

    public UserDto register(RegisterRequest request) {
        if(userService.existsByUserName(request.getUsername())) {
            throw BaseException.builder()
                    .errorMessage("Username is already used")
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        Cart cart = new Cart(
                null,
                new ArrayList<>(),
                user
        );
        user.setCart(cart);
        return userDtoConverter.convert(userRepository.save(user));
    }

}
