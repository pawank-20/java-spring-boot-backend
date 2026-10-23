# Understanding the code
## Setup Instructions 
- To run the project : `.\mvnw.cmd spring-boot:run`
- To run the tests : `.\mvnw.cmd test`


### understanding the project strucutre.

- src/main/java

    Your application’s Java source code. Controllers and other Java classes will go here.

- src/main/resources

    Application configuration and non-Java resources.

- application.properties

    Configuration settings. Currently, it contains only spring.application.name=slow, which names the application.
- src/test/java

    Automated tests for the application. We will examine the generated test later.

- static and templates

    Places for static files and server-rendered HTML templates. We do not need either for our JSON API right now.

### CSRF protection : 
Because browsers automatically attach cookies, another website could potentially trick a logged-in browser into sending a request. CSRF protection requires a separate token for state-changing requests.

In the SecurtiyConfig.java see this line : 
```
.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
```
This creates a separate XSRF-TOKEN cookie. Unlike the JWT cookie, this cookie is intentionally readable by the client so it can send the token in an X-XSRF-TOKEN request header.

---

### Authentication & API Workflow Guide
1. Obtain a CSRF Token
Request: GET http://localhost:8080/auth/csrf

Action: Copy the token value from the JSON response. Postman should also automatically store the XSRF-TOKEN cookie.

2. Log In
Request: POST http://localhost:8080/auth/login

Headers:

X-XSRF-TOKEN: <token from step 1>

Body: Use your existing JSON credentials.

Verification: Confirm the response sets the ACCESS_TOKEN cookie. The JWT should no longer be returned directly in the JSON response body.

3. Call a Protected Endpoint
Request: GET http://localhost:8080/tasks

Action: Do not send an Authorization header. Postman's cookie jar will automatically attach and send the ACCESS_TOKEN.

4. Log Out
Step A: Fetch a fresh CSRF token via GET http://localhost:8080/auth/csrf.

Step B: Call POST http://localhost:8080/auth/logout with the header X-XSRF-TOKEN set to that newly fetched value.

Verification: Confirm that the ACCESS_TOKEN is expired inside Postman's cookie jar. Then, call GET /tasks again; it should return a 401 Unauthorized status.

---

## How auth works right now (code walkthrough)

**Short answer:** the flow is complete. You won't find an `if (jwt is valid)` or `if (csrf matches)` in our own code, because **Spring Security's filter chain does both checks before the request reaches a controller**. Our code only configures *where* to find the tokens and *how* to verify them.

### The pieces

| File | Role |
|---|---|
| `SecurityConfig.java` | Defines the `JwtEncoder`/`JwtDecoder` (HS256, secret from `app.jwt.secret`), the BCrypt `PasswordEncoder`, and the `SecurityFilterChain` (CSRF, stateless sessions, `/auth/**` public, everything else needs auth, JWT resource server). |
| `AuthService.java` | `register()` hashes the password with BCrypt and saves the user. `login()` checks the password with `passwordEncoder.matches(...)` and **creates** a signed JWT (`iss=slow-api`, `sub=<username>`, `exp=now+1h`). |
| `AuthController.java` | `/auth/csrf` returns a CSRF token, `/auth/login` puts the JWT in an `ACCESS_TOKEN` HttpOnly cookie, `/auth/logout` overwrites that cookie with an expired one. |
| `CookieJwtBearerTokenResolver.java` | Tells Spring Security to read the JWT from the `ACCESS_TOKEN` cookie instead of the `Authorization: Bearer` header. |

### Where is the CSRF token checked?

In Spring's **`CsrfFilter`**, turned on by `.csrf(...)` in `SecurityConfig`.

- For every **state-changing** request (`POST`, `PUT`, `PATCH`, `DELETE`), it compares the `X-XSRF-TOKEN` header with the value in the `XSRF-TOKEN` cookie (via `CookieCsrfTokenRepository`). If they're missing or don't match, it returns **403** and the controller never runs.
- `GET`/`HEAD`/`OPTIONS`/`TRACE` aren't checked.
- `permitAll()` on `/auth/**` does **not** turn CSRF off. That's why `POST /auth/login`, `/auth/register` and `/auth/logout` also need the `X-XSRF-TOKEN` header.
- `CsrfTokenRequestAttributeHandler` (not the default `Xor...` handler) means the raw token is accepted, so you can copy the cookie value straight into the header.

### Where is the JWT checked?

In Spring's **`BearerTokenAuthenticationFilter`**, turned on by `.oauth2ResourceServer(...jwt...)`:

1. The filter calls `CookieJwtBearerTokenResolver.resolve()` to get the token from the `ACCESS_TOKEN` cookie.
2. If there's no token, the request stays anonymous. `.anyRequest().authenticated()` then rejects it with **401**, except on `/auth/**`.
3. If there is a token, `JwtAuthenticationProvider` passes it to our `JwtDecoder` bean (`NimbusJwtDecoder`, same secret, HS256). The decoder:
   - verifies the **signature**, so a tampered token or one signed with a different secret fails,
   - checks the **time claims** (`exp` and `nbf`, with about 60s clock skew) using the default validators.
4. If either check fails, you get **401**. If both pass, a `JwtAuthenticationToken` with `sub` = username is put in the `SecurityContext` and the request moves on to the controller.

`SessionCreationPolicy.STATELESS` means none of this is remembered between requests. Every request goes through the full check again.

### Request flow

```
Request
  └─ CsrfFilter            → state-changing request without a valid X-XSRF-TOKEN? → 403
  └─ BearerTokenAuthenticationFilter
        └─ CookieJwtBearerTokenResolver (reads ACCESS_TOKEN cookie)
        └─ JwtDecoder (signature + exp)  → invalid? → 401
  └─ AuthorizationFilter   → not /auth/** and not authenticated? → 401
  └─ Controller
```

### Gaps and limitations (it works, but these are worth knowing)

- **Logout doesn't revoke the token.** It only tells the browser to delete the cookie. If someone copied the JWT, it still works until `exp` (up to 1h). There's no denylist or token versioning.
- **Issuer isn't validated.** `iss=slow-api` is set on login, but the decoder doesn't check it (no `JwtValidators.createDefaultWithIssuer(...)`). Since the token is signed with our own secret, this matters very little right now.
- **Tasks aren't tied to a user.** `TaskController`/`TaskService` never read the authenticated principal, so any logged-in user can see and change every task. Authentication works, but there's no per-user authorization.
- **Secret has a hard-coded default** in `application.properties` (`JWT_SECRET` fallback). Fine for local dev, but it must be overridden in any real deployment.
- **Cookie `secure(false)`** is only for local HTTP. Set it to `true` behind HTTPS.
- **No refresh token.** When the 1h JWT expires, the user has to log in again.

---
Summary of the auth flow so far : 

The auth flow is complete. You won't find a JWT or CSRF check in your own code because Spring Security's filters do both before a request reaches any controller. Your code only tells Spring where to find each token and how to verify it. 

Where the CSRF token is checked: Spring's CsrfFilter, which .csrf(...) in SecurityConfig.java turns on.

On every POST, PUT, PATCH or DELETE it compares the X-XSRF-TOKEN header with the XSRF-TOKEN cookie. If they don't match, it returns 403.
permitAll() on /auth/** does not turn CSRF off, so the login, register and logout requests need the header too.
Where the JWT is checked: Spring's BearerTokenAuthenticationFilter, which .oauth2ResourceServer(...jwt...) turns on.

It calls CookieJwtBearerTokenResolver.java to read the ACCESS_TOKEN cookie.
It passes the token to your JwtDecoder bean, which checks the HS256 signature and the expiry time.
A missing, tampered or expired token gets a 401.
