package com.example.slow;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection="tasks")
//@Document(collection = "tasks") tells Spring Data to store these objects in the MongoDB tasks collection.
public class Task{
@Id  //@Id marks id as the document's unique identifier. MongoDB uses this to identify individual records.
public String id; // changed id from Long to String. MongoDB normally generates an ObjectId, which is represented as a string in our API. We no longer need AtomicLong to generate IDs.
public String title;
public String priority;
// The no-argument constructor Task() {} allows Spring to create an empty Task when reading data from MongoDB.
public Task(){

}
public Task ( String id,String title, String priority){
    this.id=id;
    this.title=title;
    this.priority=priority;
}
}