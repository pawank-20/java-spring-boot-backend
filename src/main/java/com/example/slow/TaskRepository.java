
package com.example.slow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByOwnerUsername(String ownerUsername);

    Optional<Task> findByIdAndOwnerUsername(String id, String ownerUsername);
}
/*An interface defines a set of operations without implementing them itself. Here, Spring Data supplies the implementation for us.
MongoRepository<Task, String> means:
- Task: the type of document being stored.
- String: the type of its ID.
This gives us methods such as save(), findAll(), findById(), and deleteById() without writing the MongoDB queries ourselves.
*/