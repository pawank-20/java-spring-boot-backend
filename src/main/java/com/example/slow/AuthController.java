
package com.example.slow;

import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Calling GET /auth/csrf makes Spring generate the CSRF token and return it in
    // the response body. The response should also set the XSRF-TOKEN cookie.
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) {
        return Map.of("token", csrfToken.getToken());
    }

    /*
     * Registration returns a confirmation, not the user object, so the password
     * hash is never sent back to the client. Login returns the token.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request.username, request.password);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(
                request.username, request.password);

        ResponseCookie cookie = ResponseCookie
                .from("ACCESS_TOKEN", token)
                .httpOnly(true) // httpOnly(true): browser JavaScript cannot read the JWT cookie.
                .secure(false) // allows local HTTP testing. Use true in production over HTTPS.
                .path("/") // the cookie is available across your API paths.
                .sameSite("Lax") // restricts some cross-site cookie sending
                .maxAge(Duration.ofHours(1)) // the cookie expires after one hour.
                .build();
        // The response now contains a Set-Cookie header rather than exposing the JWT in
        // the JSON response
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

    // This expires the JWT cookie in the client. It doesn't revoke a previously
    // copied JWT; that token remains usable until its own expiry unless we add a
    // server-side revocation mechanism. We'll discuss that trade-off later.
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        ResponseCookie cookie = ResponseCookie
                .from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out"));
    }
}
