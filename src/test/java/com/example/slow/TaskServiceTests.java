
package com.example.slow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTests {

    @Test
    //@Test marks a method that JUnit should execute as a test.
    void createsAndFindsTask() {
        TaskService service = new TaskService();
        //- new TaskService() is intentional here: we're testing the service directly, so we don't need Spring to create it.

        Task created = service.create("Learn Java", "High");
        Task found = service.findById(created.id);

        assertNotNull(found);
        //assertEquals() checks whether two values match.
        assertEquals("Learn Java", found.title);
        assertEquals("High", found.priority);
    }

    @Test
    void returnsNullForMissingTask() {
        TaskService service = new TaskService();

        Task found = service.findById(999L);

        assertNull(found);
    }
}
