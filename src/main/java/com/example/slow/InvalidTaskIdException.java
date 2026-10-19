package com.example.slow;

// RuntimeException is Java's base class for unchecked application errors. We're defining a specific exception so the API can distinguish a malformed ID from a valid ID that simply doesn't exist.
public class InvalidTaskIdException extends RuntimeException{
    public InvalidTaskIdException(String id){
        super("Invalid Task Id: "+id);
    }
}
