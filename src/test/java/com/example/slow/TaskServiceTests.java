
package com.example.slow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//These are unit tests: they use a fake repository, so they don't need MongoDB to run.
class TaskServiceTests {

    @Test
     //@Test marks a method that JUnit should execute as a test.
    void createsAndFindsTask() {
        TaskRepository repository =
                org.mockito.Mockito.mock(TaskRepository.class);

        TaskService service = new TaskService(repository);

        Task saved = new Task("test-id", "Learn Java", "High");

        org.mockito.Mockito.when(repository.save(
                org.mockito.ArgumentMatchers.any(Task.class)))
                .thenReturn(saved);

        org.mockito.Mockito.when(repository.findById("test-id"))
                .thenReturn(java.util.Optional.of(saved));

        Task created = service.create("Learn Java", "High");
        Task found = service.findById(created.id);

        assertNotNull(found);
        assertEquals("Learn Java", found.title);
        assertEquals("High", found.priority);
    }

    @Test
    void returnsNullForMissingTask() {
        TaskRepository repository =
                org.mockito.Mockito.mock(TaskRepository.class);

        TaskService service = new TaskService(repository);

        org.mockito.Mockito.when(repository.findById("missing-id"))
                .thenReturn(java.util.Optional.empty());

        Task found = service.findById("missing-id");

        assertNull(found);
    }
}
