package com.example.slow;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
// @RestController tells Spring that this class handles web requests and that
// its return values should be written into HTTP responses. Without it, Spring
// won't treat this class as a REST controller
public class HelloController {
    @GetMapping("/hello")
    // public String sayHello() is an ordinary Java method. It returns a String, and
    // Spring MVC converts that return value into the response body.
    public String sayHello() {
        return "yo";
    }

    @GetMapping("/abc/{name}")
    public String sayParams(@PathVariable String name) { // @PathVariable tells Spring to take that
        // value from the URL and pass it into the Java method as the name argument. In
        // express it is req.param.name
        return name;
    }

    @GetMapping("/def")
    // if in the url you do not provide any query param , it will give you an error
    // with code 400. to prevent that do : (@RequestParam(required = false) String
    // name )
    public String queryParam(@RequestParam String name) {
        // in express it is req.query.name
        return "Hello " + name;
    }

    @GetMapping("/ghi")
    public Map<String, String> sayHello(
            @RequestParam(required = false, defaultValue = "World") String name) {

        return Map.of(
                "message", "Hello " + name,
                "language", "Java");
        // returns an object : {"language":"Java","message":"Hello World"}
    }

    @PostMapping("/tasks")
    public Map<String, Object> createTask(@RequestBody Map<String, Object> task) {
        // @RequestBody tells Spring to read the incoming JSON body and convert it into a Java object—in this case, a Map.
        return Map.of(
                "message", "Task received",
                "task", task
        );
        
    }
    
}
