package com.example.slow;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@Component
public class CookieJwtBearerTokenResolver implements BearerTokenResolver {
    @Override
    // The important method is resolve(). Spring Security calls it when it needs an access token. We search the incoming request's cookies and return the JWT value. If the cookie isn't present, we return null, and the protected endpoint remains unauthenticated.
    // This is an implementation of BearerTokenResolver [an interface that tells Spring Security where to find a bearer token]. We're reusing Spring's existing JWT verification rather than writing our own token validator.
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("ACCESS_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
