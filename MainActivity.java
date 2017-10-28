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

public class MainActivity extends AppCompatActivity {

    public writeToFireBase mWrite = new writeToFireBase();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Post post01 = new Post("this is the first post!\n",user.getUid());
    Post post02 = new Post("this is the second post!\n",user.getUid());
    Post post03 = new Post("this is the third post!\n",user.getUid());
    Post post04 = new Post("this is the fourth post!\n",user.getUid());
    Post post05 = new Post("this is the fifth post!\n",user.getUid());
    Post post06 = new Post("this is the sixth post!\n",user.getUid());
//    User user01 = new User("FeiChen");
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWrite.writePosts("01",post01);
        mWrite.writePosts("02",post02);
        mWrite.writePosts("03",post03);
        mWrite.writePosts("04",post04);
        mWrite.writePosts("05",post05);
        mWrite.writePosts("06",post06);
        al = new ArrayList<>();
        al.add(post01.getPostText());
        al.add(post02.getPostText());
        al.add(post03.getPostText());
        al.add(post04.getPostText());
        al.add(post05.getPostText());
        al.add(post06.getPostText());

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
//            Database data;
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("users");
//            myRef.child("users").child(userId).setValue(user);

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Nope" , Toast.LENGTH_SHORT).show();
                mWrite.dislikePost("01");
//                userRef.child("cf0906").push();
//                mWrite.loginUser();
//                postRef.child(user01.getName()).child("attitude").child("Dislike").setValue(2);
//                userRef.child(user.getUid()).setValue("on");

//                data.getRef();
//                data.addSelfToCommunity("690", "liuzulin");
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
