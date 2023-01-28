package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.auth.TokenResponseDto;
import com.example.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseDtoConverter {

    private final UserDtoConverter converter;

    public TokenResponseDtoConverter(UserDtoConverter converter) {
        this.converter = converter;
    }

    public TokenResponseDto convert(String token, User user) {
        return new TokenResponseDto(
                token,
                converter.convert(user)
        );
    }

}
