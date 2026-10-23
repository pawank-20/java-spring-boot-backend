
package com.example.slow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        // The constructor receives TaskRepository from Spring through dependency
        // injection. You don't instantiate the repository yourself.
    }

    private void validateId(String id) {
        if (id == null || !id.matches("[0-9a-fA-F]{24}")) {
            throw new InvalidTaskIdException(id);
        }
    }

    public Task create(String title, String priority, String ownerUsername) {
        Task task = new Task(null, title, priority, ownerUsername);// MongoDB generates tje ID when save() inserts the
                                                                   // document.
        Task savedTask = taskRepository.save(task);

        log.info("Created task with id {}", savedTask.id);
        return savedTask;
    }

    public List<Task> findAll(String ownerUsername) {
        return taskRepository.findAllByOwnerUsername(ownerUsername);
    }

    public Task findById(String id, String ownerUsername) {
        validateId(id);

        return taskRepository
                .findByIdAndOwnerUsername(id, ownerUsername)
                .orElse(null);
    }

    public Task update(
            String id,
            String title,
            String priority,
            String ownerUsername) {

        Task task = findById(id, ownerUsername);

        if (task == null) {
            return null;
        }

        task.title = title;
        task.priority = priority;

        return taskRepository.save(task);
        // save(task) inserts a new document or updates the existing document when its
        // ID is already set.
    }

    public boolean delete(String id, String ownerUsername) {
    Task task = findById(id, ownerUsername);

    if (task == null) {
        return false;
    }

    taskRepository.delete(task);
    return true;
}
}
