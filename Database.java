package com.example.meetgo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nanto on 10/27/2017.
 */

public class Database {
    public FirebaseDatabase mDatabase;
    public DatabaseReference myRef;
//    public String ID = "690";
    public Database(){
    }

//    myRef.setValue("Hello, World!");
    public void getRef(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.setValue("users");
    }

    public void addSelfToCommunity(String userId, String name) {
        User user = new User(name);
        myRef.child("users").child(userId).setValue(user);
    }
}
