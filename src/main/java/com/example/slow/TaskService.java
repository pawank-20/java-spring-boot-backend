
package com.example.slow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log =
            LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        //The constructor receives TaskRepository from Spring through dependency injection. You don't instantiate the repository yourself.
    }

    public Task create(String title, String priority) {
        Task task = new Task(null, title, priority); //MongoDB generates tje ID when save() inserts the document.
        Task savedTask = taskRepository.save(task);

        log.info("Created task with id {}", savedTask.id);
        return savedTask; 
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task update(String id, String title, String priority) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }

        task.title = title;
        task.priority = priority;

        return taskRepository.save(task);
        //save(task) inserts a new document or updates the existing document when its ID is already set.
    }

    public boolean delete(String id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }
}
