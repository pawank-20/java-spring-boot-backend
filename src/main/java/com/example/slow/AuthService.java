package com.example.slow;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;

    }

    public void register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists");
        }

        String passwordHash = passwordEncoder.encode(password);

        AppUser user = new AppUser(username, passwordHash);
        userRepository.save(user);
    }

    /*
     * The login process has two separate jobs:
     * 1. Verify the submitted password against the stored BCrypt hash.
     * 2. If it matches, issue a signed token with the username as its subject and
     * an expiry time one hour in the future.
     */
    public String login(String username, String password) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or Password"));
        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        }
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("slow-api")
                .subject(user.username)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }

}
