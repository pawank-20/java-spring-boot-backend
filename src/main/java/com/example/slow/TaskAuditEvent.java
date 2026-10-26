
package com.example.slow;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// we are creating another collection so that we can learn about transactions 
@Document(collection = "task_audit")
public class TaskAuditEvent {

    @Id
    public String id;

    public String taskId;
    public String action;
    public String username;

    public TaskAuditEvent() {
    }

    public TaskAuditEvent(
            String taskId, String action, String username) {
        this.taskId = taskId;
        this.action = action;
        this.username = username;
    }
}
