package com.example.keval.gomeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private EditText editText;
    private EditText editText2;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    //private FirebaseAuth.AuthStateListener mAuthListener;
    //private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        progressDialog=new ProgressDialog(this);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        editText2=(EditText)findViewById(R.id.editText2);
        textViewSignin=(TextView)findViewById(R.id.textViewSignin);
        button.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        //to check whenever the user signs in or out
        /*firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };*/
    }

    private void registerUser(){
        String email = editText.getText().toString().trim();
        String password=editText2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(MainActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else{
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(MainActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this,"Could not register. Please try again.",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==button){
            registerUser();
        }

        if(v==textViewSignin){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
