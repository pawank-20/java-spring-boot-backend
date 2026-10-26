
package com.example.slow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestControllerAdvice
// @RestControllerAdvice lets this class handle exceptions thrown by multiple
// controllers.
public class ApiExceptionHandler {
        private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

        // @ExceptionHandler(...) selects which exception this method handles. We're
        // telling Spring: "When the exception type is MethodArgumentNotValidException,
        // use this handler."
        @ExceptionHandler(MethodArgumentNotValidException.class)
        // The method extracts the first validation error and returns a JSON response
        // such as {"error":"Title is required"} with status 400.
        public ResponseEntity<Map<String, String>> handleValidation(
                        MethodArgumentNotValidException exception) {

                String message = exception.getBindingResult()
                                .getFieldErrors()
                                .get(0)
                                .getDefaultMessage();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", message));
        }

        @ExceptionHandler(InvalidTaskIdException.class)
        public ResponseEntity<Map<String, String>> handleInvalidTaskId(
                        InvalidTaskIdException exception) {

                return ResponseEntity
                                .badRequest()
                                .body(Map.of("error", exception.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleUnexpected(
                        Exception exception) {

                log.error("Unexpected application error", exception);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Internal server error"));
        }
}

/*
 * 
 * THis is the error middleware. there are three types of middlewares in spring.
 * Interceptors, filters and controller advice. in express the equivalent of
 * this was :
 * app.use((err, req, res, next) => {
 * res.status(err.status || 500).json({
 * error: err.message
 * });
 * });
 * 
 */
