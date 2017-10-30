//package com.example.meetgo;
//
///**
// * Created by nanto on 10/28/2017.
// */
//import android.util.Log;
//
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//import static android.content.ContentValues.TAG;
//
//public class readFromFireBase {
//    private DatabaseReference mDatabase;
//    private ValueEventListener postListener;
////    private Post postRead;
////    private FirebaseDatabase dataBase;
//    private String postText;
//
//    public readFromFireBase(String ID) {
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(ID).child("postText");
//        postText = "default"+ID;
////        this.postRead = new Post("","");
//        postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
////                Post post = dataSnapshot.getValue(Post.class);
////                postText = post.getPostText();
//                  postText = dataSnapshot.getValue(String.class);
//                // ...
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        mDatabase.addValueEventListener(postListener);
//    }
//
//    public String readPostText(){
//        return postText;
//    }
//
//}