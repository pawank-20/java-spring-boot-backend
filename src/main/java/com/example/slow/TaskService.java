
package com.example.slow;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TaskService {

    private Map<Long, Task> tasks = new HashMap<>();
    private long nextId = 1;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public Task create(String title, String priority) {
        Task task = new Task(nextId, title, priority);

        tasks.put(nextId, task);
        nextId++;
// {} is a placeholder for a value supplied to the logger. This is preferable to concatenating strings, and the logger includes useful metadata such as severity and timestamp.
        log.info("Created task with id {}", task.id);
//Common levels are DEBUG for diagnostic details, INFO for normal events, WARN for unexpected situations, and ERROR for failures.

log.debug("Creating task with title {}", title);
        return task;
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
        // see notes about generics.
    }

    public Task findById(Long id) {
        return tasks.get(id);
    }

    public Task update(Long id, String title, String priority) {
        if (!tasks.containsKey(id)) {
            return null;
        }

        Task task = new Task(id, title, priority);
        tasks.put(id, task);

        return task;
    }

    public boolean delete(Long id) {
        if (!tasks.containsKey(id)) {
            return false;
        }

        tasks.remove(id);
        return true;
    }
}
