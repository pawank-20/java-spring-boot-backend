
package com.example.slow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
//@RestControllerAdvice lets this class handle exceptions thrown by multiple controllers.
public class ApiExceptionHandler {
//@ExceptionHandler(...) selects which exception this method handles. We're telling Spring: "When the exception type is MethodArgumentNotValidException, use this handler."
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //The method extracts the first validation error and returns a JSON response such as {"error":"Title is required"} with status 400.
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
}
