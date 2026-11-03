package com.example.slow;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

//Why bother with this class? Previously, we extracted values using body.get("title") and body.get("priority"). Now Spring can convert incoming JSON into a TaskRequest object.
// @Schema provides descriptions and examples for Swagger. It doesn't perform validation; @NotBlank still handles that.
@Schema(description = "Information required to create or update a task")
public class TaskRequest {

    @Schema(description = "Task title", example = "Review compliance report")
    @NotBlank(message = "Title is required")
    public String title;

    @Schema(description = "Task priority", example = "High")
    @NotBlank(message = "Priority is required")
    public String priority;
}
