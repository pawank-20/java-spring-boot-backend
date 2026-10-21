
package com.example.slow;

import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
    Registration returns a confirmation, not the user object, so the password hash is never sent back to the client. Login returns the token.
    */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request.username, request.password);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered"));
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(
                request.username, request.password);

        return Map.of(
                "accessToken", token,
                "tokenType", "Bearer");
    }
}
