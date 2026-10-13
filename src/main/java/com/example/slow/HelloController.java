package com.example.slow;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController 
//@RestController tells Spring that this class handles web requests and that its return values should be written into HTTP responses. Without it, Spring won't treat this class as a REST controller
public class HelloController {
    @GetMapping("/hello")
    //public String sayHello() is an ordinary Java method. It returns a String, and Spring MVC converts that return value into the response body.
    public String sayHello() {
        return "yo";
    }
    
}
