
package com.example.slow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;


import java.util.List;

/*
Notice how we defined a constructor here but not inside the service : 
We explicitly wrote a constructor because the controller needs a TaskService object supplied to it. Spring creates the service and supplies it to the controller. This is dependency injection.
If we instead wrote new TaskService() inside the controller, the controller would create its own service instance. That would undermine Spring's management of the service and could cause separate in-memory task stores.

*/


/*
Authentication is provided by Spring Security for an authenticated request. Because your JWT subject is the username, authentication.getName() returns that username. The client cannot choose another owner simply by changing the JSON body.
*/
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    // The constructor receives TaskService from Spring; this is called dependency
    // injection [Spring supplies a class with the object it depends on, rather than
    // requiring that class to create it itself].

    @PostMapping
    // ResponseEntity lets us control HTTP status codes as well as response bodies.

    // spring will now convert the incoming req.body to TaskRequest Object
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        Task task = taskService.create(request.title, request.priority, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    // see comments for the params above
    public List<Task> findAll(Authentication authentication) {
        return taskService.findAll(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable String id, Authentication authentication) {
        Task task = taskService.findById(id, authentication.getName());

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @Valid @PathVariable String id,
            @RequestBody TaskRequest request, Authentication authentication) {

        Task task = taskService.update(id, request.title, request.priority, authentication.getName());

        if (task == null) {
            return ResponseEntity.notFound().build();
            // This constructs a response with HTTP status 404 Not Found and no response
            // body.
            // similarly :
            // return ResponseEntity.noContent().build(); returns 204 No Content.
        }

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        boolean deleted = taskService.delete(id, authentication.getName());

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
