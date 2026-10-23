
package com.example.slow;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; //add a BCrypt password encoder bean
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // @Configuration tells Spring this class defines application configuration.
public class SecurityConfig {

    @Bean
    SecretKey jwtSecretKey(
            @Value("${app.jwt.secret}") String secret) {
        return new SecretKeySpec(
                secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /*
     * 
     * @Bean
     * PasswordEncoder passwordEncoder() {
     * return new BCryptPasswordEncoder();
     * }
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // SecurityFilterChain defines which requests are public and which require
    // authentication. /auth/** is public so users can register and log in; other
    // routes require a valid JWT.

    // Because browsers automatically attach cookies, another website could
    // potentially trick a logged-in browser into sending a request. CSRF protection
    // requires a separate token for state-changing requests.

    SecurityFilterChain securityFilterChain(HttpSecurity http, CookieJwtBearerTokenResolver cookieTokenResolver)
            throws Exception {
        // This creates a separate XSRF-TOKEN cookie. Unlike the JWT cookie, this cookie
        // is intentionally readable by the client so it can send the token in an
        // X-XSRF-TOKEN request header.
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                        //This creates a separate XSRF-TOKEN cookie. Unlike the JWT cookie, this cookie is intentionally readable by the client so it can send the token in an X-XSRF-TOKEN request header.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // STATELESS tells Spring not to
                                                                                 // maintain a server-side login session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .bearerTokenResolver(cookieTokenResolver)
                        .jwt(Customizer.withDefaults()));
        // oauth2ResourceServer(...jwt...) activates Spring Security's built-in JWT
        // verification. It checks the token before the request reaches your controller.

        return http.build();
    }
}
