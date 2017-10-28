package com.example.meetgo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanto on 10/27/2017.
 */

public class User {
    private String username;
    //    public String email;
    private List<Post> likes;
    private List<Post> dislikes;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username) {
        this.username = username;
//        this.email = email;
    }

    public String getName(){
        return this.username;
    }


}
