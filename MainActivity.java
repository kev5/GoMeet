package com.example.meetgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public writeToFireBase mWrite = new writeToFireBase();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Post> postsToWrite;
    Post post01 = new Post("this is the very first post!\n",user.getUid());
    Post post02 = new Post("this is the second post!\n",user.getUid());
    Post post03 = new Post("this is the third post!\n",user.getUid());
    Post post04 = new Post("this is the fourth post!\n",user.getUid());
    Post post05 = new Post("this is the fifth post!\n",user.getUid());
    Post post06 = new Post("this is the sixth post!\n",user.getUid());
    private ArrayList<String> al;
    private ArrayList<Post> postArray;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWrite.writePosts("1",post01);
        mWrite.writePosts("2",post02);
        mWrite.writePosts("3",post03);
        mWrite.writePosts("4",post04);
        mWrite.writePosts("5",post05);
        mWrite.writePosts("6",post06);
        al = new ArrayList<>();
        al.add(post01.getPostText());
//        al.add(post02.getPostText());
//        al.add(post03.getPostText());
//        al.add(post04.getPostText());
//        al.add(post05.getPostText());
//        al.add(post06.getPostText());

        ValueEventListener postListener01;
        DatabaseReference ref01 = FirebaseDatabase.getInstance().getReference("posts").child("01").child("postText");
        postListener01 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post text
                String str = dataSnapshot.getValue(String.class);
                al.add(str);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref01.addValueEventListener(postListener01);

        ValueEventListener postListener02;
        DatabaseReference ref02 = FirebaseDatabase.getInstance().getReference("posts").child("02").child("postText");
        postListener02 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post text
                String str = dataSnapshot.getValue(String.class);
                al.add(str);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref02.addValueEventListener(postListener02);
//
//        ArrayList<ValueEventListener> postListenerList;
//        postListenerList = new ArrayList<ValueEventListener>();
//        ArrayList<DatabaseReference> refList;
//        refList = new ArrayList<DatabaseReference>();
//        for (int j=0;j<6;j++){
//            refList.set(j,(FirebaseDatabase.getInstance().getReference("posts").child("0"+j).child("postText")));
//            postListenerList.set(j, new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Get Post text
//                    String str = dataSnapshot.getValue(String.class);
//                    al.add(str);
//                    // ...
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                    // ...
//                }
//            });
//
//            refList.get(j).addValueEventListener(postListenerList.get(j));
//        }


//        readFromFireBase mRead = new readFromFireBase("1");
//        al.add(mRead.readPostText());
//        readFromFireBase mRead;
//        for(int j=0; j<6; j++){
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child(""+j);
//            postListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Get Post Text
//                String str = dataSnapshot.getValue(String.class);
//                al.add(str);
//                    // ...
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                    // ...
//                }
//            };
//            ref.addValueEventListener(postListener);
//        }
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Nope" , Toast.LENGTH_SHORT).show();
                mWrite.dislikePost("01");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Like!", Toast.LENGTH_SHORT).show();
                mWrite.likePost("01");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
