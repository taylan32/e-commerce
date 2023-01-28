package com.example.ecommerce.service;

import com.example.ecommerce.dto.converter.UserDtoConverter;
import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter converter;


    public UserService(UserRepository userRepository, UserDtoConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> BaseException
                .builder()
                .errorMessage("Username not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build()
        );
    }

    public UserDto getUserByUsername(String username) {
        return converter.convert(findUserByUsername(username));
    }

    public User findUserInContext() {
        final Authentication authentication = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> BaseException
                        .builder()
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .errorMessage("You must login.")
                        .build());

        final UserDetails details = Optional.ofNullable((UserDetails) authentication.getDetails())
                .orElseThrow(() -> BaseException
                        .builder()
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .errorMessage("You must login.")
                        .build());

        return findUserByUsername(details.getUsername());
    }

    protected boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }


}
