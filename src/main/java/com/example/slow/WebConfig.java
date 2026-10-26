package com.example.slow;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

//Creating an interceptor class doesn't automatically make it active. We need to register it with Spring MVC.
// WebMvcConfigurer lets us customize Spring MVC. The path patterns mean this interceptor applies to /tasks, /tasks/123, and other paths under /tasks.
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TaskInterceptor())
                .addPathPatterns("/tasks/**");
    }
}