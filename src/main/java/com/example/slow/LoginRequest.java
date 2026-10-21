package com.example.slow;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank 
    public String username;
    @NotBlank 
    public String password;
}

//These DTOs [data transfer objects] define what the client is allowed to send. The registration request enforces a minimum password length; login checks that both fields are present.