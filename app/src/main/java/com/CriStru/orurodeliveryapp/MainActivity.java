package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public TextView listar;
    public Button logout;
    private DatabaseReference dbOruro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        dbOruro = FirebaseDatabase.getInstance().getReference().child("Usuario").child(currentUser.getUid());
        listar = findViewById(R.id.listar);
        logout = findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        dbOruro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String valor = dataSnapshot.getValue().toString();
                listar.setText(valor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Error!", databaseError.toException());
            }
        });
    }
        private void updateUI(FirebaseUser currentUser) {
            if (currentUser == null){
                Intent main=new Intent(MainActivity.this,DestinyLogin.class);
                startActivity(main);
            }
            else {

            }
        }

        private void signOut() {
            mAuth.signOut();
            updateUI(null);
        }
}
