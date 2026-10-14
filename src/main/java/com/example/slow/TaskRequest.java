package com.example.slow;


//Why bother with this class? Previously, we extracted values using body.get("title") and body.get("priority"). Now Spring can convert incoming JSON into a TaskRequest object.
public class TaskRequest {
    public String title;
    public String priority;

}
