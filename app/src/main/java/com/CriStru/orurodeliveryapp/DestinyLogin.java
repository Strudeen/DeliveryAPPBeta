package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DestinyLogin extends AppCompatActivity {
    Button btnSingIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destiny_login);
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
}