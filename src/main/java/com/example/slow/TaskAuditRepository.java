
package com.example.slow;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskAuditRepository
        extends MongoRepository<TaskAuditEvent, String> {
}