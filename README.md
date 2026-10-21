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
