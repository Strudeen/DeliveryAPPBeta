package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DestinyLogin extends AppCompatActivity {
    Button btnSingIn, btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_destiny_login);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup=new Intent(DestinyLogin.this, SignUpActivity.class);
                startActivity(signup);
            }
        });
        btnSingIn=findViewById(R.id.btnSignIn);
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=new Intent(DestinyLogin.this, SignInActivity.class);
                startActivity(signin);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent main=new Intent(DestinyLogin.this,MainActivity.class);
            startActivity(main);
        }
    }
}