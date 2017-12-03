package com.example.meetgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    private EditText mEventName;
    private Button mPostEvent;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //CheckUserSex();
        mAuth = FirebaseAuth.getInstance();
        mEventName = (EditText) findViewById(R.id.event_name);
        mPostEvent = (Button) findViewById(R.id.go_create);
        mPostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eventName = mEventName.getText().toString();
                final String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference postDb = FirebaseDatabase.getInstance().getReference().child("posts").child(userId).child("postText");
                postDb.setValue(eventName);
                Intent intent = new Intent(PostActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
//    private String userSex;
//    private String notUserSex;
//    public void CheckUserSex(){
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
//        maleDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if(dataSnapshot.getKey().equals(user.getUid())){
//                    userSex = "Male";
//                    notUserSex = "Female";
//
//                }
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
//        femaleDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if(dataSnapshot.getKey().equals(user.getUid())){
//                    userSex = "Female";
//                    notUserSex = "Male";
//
//                }
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
}
