package com.example.meetgo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nanto on 10/28/2017.
 */

public class writeToFireBase {
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference writepostRef;
    private DatabaseReference userRef;

    public writeToFireBase(){
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.database = FirebaseDatabase.getInstance();
        this.writepostRef = this.database.getReference("posts");
        this.userRef = this.database.getReference("users");
    }

    public void writePosts(String postID, Post post){
        this.writepostRef.child(postID).setValue(post);
    }

    public void likePost(String postID){
        DatabaseReference commentPostRef = this.writepostRef.child(postID);
        commentPostRef.child("likes").push().setValue(this.user.getUid());
    }

    public void dislikePost(String postID){
        DatabaseReference commentPostRef = this.writepostRef.child(postID);
        commentPostRef.child("dislikes").push().setValue(this.user.getUid());
    }

    public void loginUser(){
        this.userRef.child(user.getUid()).setValue("on");
    }
}
