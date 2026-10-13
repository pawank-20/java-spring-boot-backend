
package com.example.slow;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private Map<Long, Task> tasks = new HashMap<>();
    private long nextId = 1;

    public Task create(String title, String priority) {
        Task task = new Task(nextId, title, priority);

        tasks.put(nextId, task);
        nextId++;

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
