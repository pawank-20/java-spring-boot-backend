package com.example.slow;

import jakarta.validation.constraints.NotBlank;

//Why bother with this class? Previously, we extracted values using body.get("title") and body.get("priority"). Now Spring can convert incoming JSON into a TaskRequest object.
public class TaskRequest {
     @NotBlank(message = "Title is required")
    public String title;
        @NotBlank(message = "Priority is required")
    public String priority;
    

}
