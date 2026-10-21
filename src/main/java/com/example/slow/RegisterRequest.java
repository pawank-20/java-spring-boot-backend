package com.example.slow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message="username cannot be blank")
    public String username;
    @NotBlank 
    @Size(min=8, message="Password must have atleast 8 characters.")
    public String password;
}
