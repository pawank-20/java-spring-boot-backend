package com.example.slow;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class AppUser {
    @Id 
    public String id;
    public String username;
    public String passwordHash;
    public AppUser(){


    }
    public AppUser(String username, String passwordHash){
        this.username=username;
        this.passwordHash=passwordHash;
    }
}
