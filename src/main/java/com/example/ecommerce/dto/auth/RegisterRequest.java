package com.example.ecommerce.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is now allowed to be empty")
    @Size(min = 3, max = 16, message = "Username should contain between 3 and 16 characters")
    private String username;


    @NotBlank(message = "Password is now allowed to be empty")
    @Size(min = 4, max = 16, message = "Password should contain between 4 and 16 characters")
    private String password;


    @NotBlank(message = "Name is now allowed to be empty")
    @Size(min = 3, max = 40, message = "Name should contain between 3 and 40 characters")
    private String name;


    @NotBlank(message = "Email is now allowed to be empty")
    @Email
    @Size(min = 10, max = 30, message = "Email should contain between 10 and 30 characters")
    private String email;

    @NotBlank(message = "Phone number is now allowed to be empty")
    @Size(min = 11, max = 11, message = "Invalid phone number. Phone number should contain 11 characters")
    private String phoneNumber;

}
