package com.example.ecommerce.dto.converter;

import com.example.ecommerce.dto.user.UserDto;
import com.example.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convert(User from) {
        return new UserDto(
                from.getId(),
                from.getUsername(),
                from.getName(),
                from.getEmail(),
                from.getPhoneNumber()
        );
    }

}
