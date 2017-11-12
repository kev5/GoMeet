package com.example.keval.gomeet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
//import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    //private TextView textView3;
    private Button button3;
    private Button button4;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user=firebaseAuth.getCurrentUser();

        //textView3 = (TextView)findViewById(R.id.textView3);
        //textView3.setText("Welcome "+user.getEmail());
        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==button3){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if(v==button4){
            View view = (LayoutInflater.from(ProfileActivity.this)).inflate(R.layout.create_event, null);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileActivity.this);
            alertBuilder.setView(view);

            Dialog dialog = alertBuilder.create();
            dialog.show();

            alertBuilder.setCancelable(true)
                    .setPositiveButton("GO", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog,int which){

                        }
                    });
        }


    }
}

/*public class readFromFireBase {
    private DatabaseReference mDatabase;
    private ValueEventListener postListener;
    private Post postRead;
    private FirebaseDatabase dataBase;
    private String postText;

    public readFromFireBase(String ID) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts").child(ID).child("postText");
        postText = "default"+ID;
        this.postRead = new Post("","");
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                postText = post.getPostText();
                  postText = dataSnapshot.getValue(String.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public String readPostText(){
        return postText;
    }

}*/
