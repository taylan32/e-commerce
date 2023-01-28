package com.example.ecommerce.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is now allowed to be empty")
    @Size(min = 3, max = 16, message = "Username should contain between 3 and 16 characters")
    private String username;


    @NotBlank(message = "Password is now allowed to be empty")
    @Size(min = 4, max = 16, message = "Password should contain between 4 and 16 characters")
    private String password;

}
