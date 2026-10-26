package com.example.slow;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// is a Spring web filter designed to run once per request dispatch. A filter can inspect or modify a request before it reaches your controller.
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
// MDC is a per-thread logging context. We put the request ID there so the logging system can automatically include it in log lines.
        MDC.put("requestId", requestId);
        response.setHeader("X-Request-ID", requestId);

        long startTime = System.currentTimeMillis();

        try {
            log.info("Request started: {} {}", 
                    request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info("Request completed: {} {} status={} duration={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);

            MDC.remove("requestId");
            // ensures the ID is removed from the logging context even if request processing throws an exception. This matters because server threads are reused for different requests.
        }
    }
}