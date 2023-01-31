package com.example.ecommerce.service;

import com.example.ecommerce.dto.auth.LoginRequest;
import com.example.ecommerce.dto.auth.RegisterRequest;
import com.example.ecommerce.dto.auth.TokenResponseDto;
import com.example.ecommerce.dto.converter.TokenResponseDtoConverter;
import com.example.ecommerce.dto.converter.UserDtoConverter;
import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private UserDtoConverter userDtoConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenResponseDtoConverter converter;



    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       TokenService tokenService,
                       UserDtoConverter userDtoConverter,
                       PasswordEncoder passwordEncoder,
                       TokenResponseDtoConverter converter) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.userDtoConverter = userDtoConverter;
        this.passwordEncoder = passwordEncoder;
        this.converter = converter;
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
                    .errorMessage(exception.getMessage())
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

        return userDtoConverter.convert(
                userService.create(user)
        );
    }

}
