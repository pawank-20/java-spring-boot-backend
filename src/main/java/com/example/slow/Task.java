
package com.example.slow;

public class Task {
    public Long id;
    public String title;
    public String priority;

    public Task(Long id, String title, String priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }
}

// you can also use a record. a record is an immutable thing in java. 
