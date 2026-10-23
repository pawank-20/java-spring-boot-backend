
package com.example.slow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.*;


//Testcontainers starts one MongoDB container for the test class, TaskRepositoryIntegrationTests, and reuses it across that class's test methods. When the test class finishes, Testcontainers normally stops and removes that container.
@SpringBootTest
@Testcontainers
// Testcontainers talks to Docker, which is why Docker Desktop must be running.
// notice how you never need to run docker run command yourself.
class TaskRepositoryIntegrationTests {

    @Container // @Container tells Testcontainers to manage the container's lifecycle during
               // the tests. @Container tells Testcontainers to start and stop MongoDB for the
               // test class.
    // MongoDBContainer is a Testcontainers class that knows how to run MongoDB in a
    // container.
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");
    // "mongo:7.0" is the Docker image name and version. Docker uses this image to
    // create a MongoDB container.
    // Because the class also has @Testcontainers, Testcontainers integrates with
    // JUnit to start and stop the container at the appropriate time.

    @DynamicPropertySource
    static void configureMongo(DynamicPropertyRegistry registry) {
        // registry is a DynamicPropertyRegistry, provided by Spring for registering
        // configuration values at test runtime.
        // We're telling Spring: "For this test, use this value for
        // spring.data.mongodb.uri."

        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
        // The :: syntax is called a method reference. It passes a reference to a method
        // rather than calling that method immediately.
        // in js : registry.add("spring.data.mongodb.uri", () =>
        // mongo.getReplicaSetUrl());

    }
    // Testcontainers starts MongoDB and exposes its connection details.
    // getReplicaSetUrl() supplies the appropriate connection string for that
    // container, including the details needed to connect to its replica set.

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void savesAndFindsTask() {
        Task task = new Task(null, "Learn MongoDB", "High", "alice");

        Task saved = repository.save(task);

        assertNotNull(saved.id);

        Task found = repository.findById(saved.id).orElse(null);

        assertNotNull(found);
        assertEquals("Learn MongoDB", found.title);
        assertEquals("High", found.priority);
    }

    @Test
    void updatesAndDeletesTask() {
        Task task = repository.save(
                new Task(null, "Original title", "Low", "alice"));

        task.title = "Updated title";
        repository.save(task);

        Task updated = repository.findById(task.id).orElse(null);
        assertNotNull(updated);
        assertEquals("Updated title", updated.title);

        repository.deleteById(task.id);

        assertFalse(repository.existsById(task.id));
    }
}
