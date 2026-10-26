package com.example.slow;

import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

//Creating an interceptor class doesn't automatically make it active. We need to register it with Spring MVC.
public class TaskInterceptor implements HandlerInterceptor {
    @Override 
    //preHandle() runs before the controller. Returning true allows processing to continue; returning false stops it.
    public boolean preHandle(
            HttpServletRequest req,
            HttpServletResponse res,
            Object handler) {

        System.out.println("Tasks route accessed");
        return true;
    }
}