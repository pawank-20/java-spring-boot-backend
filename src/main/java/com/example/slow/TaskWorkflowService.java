
package com.example.slow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// When another Spring-managed component calls this method through the Spring service bean, Spring starts a transaction, or joins an existing transaction if one is already active. The two repository saves participate in that transaction.
@Service
public class TaskWorkflowService {

    private final TaskRepository taskRepository;
    private final TaskAuditRepository auditRepository;

    public TaskWorkflowService(
            TaskRepository taskRepository,
            TaskAuditRepository auditRepository) {
        this.taskRepository = taskRepository;
        this.auditRepository = auditRepository;
    }

    @Transactional
    public Task createTaskAndAudit(
            String title, String priority, String username) {

        Task task = taskRepository.save(
                new Task(null, title, priority, username));

        auditRepository.save(
                new TaskAuditEvent(task.id, "CREATED", username));

        return task;
    }
}
