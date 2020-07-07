package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView tvNombre,tvDescripcion;
    private ImageView fotoCategoria;

    public TextView listar;
    public Button logout;
    private DatabaseReference dbOruro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();
        tvNombre=findViewById(R.id.tvNombreCategoria);
        tvDescripcion=findViewById(R.id.tvDescripcion);
        //nametxt = findViewById(R.id.text_name);
        //emailtxt = findViewById(R.id.text_email);
        //uidtxt = findViewById(R.id.text_uid);
        logout = findViewById(R.id.logoutButton);
        fotoCategoria=findViewById(R.id.FotoCategoria);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photo = user.getPhotoUrl();
            String Uid = user.getUid();
            //nametxt.setText(name);
            //emailtxt.setText(email);
            //uidtxt.setText(Uid);
        } else {
            goLoginScreen();
        }
        ListarCategorias();


     //Comente estas lineas porque me daba error al mostrar datos con Facebook pero ahora tambien muestra los datos de google y facebook xd asi que no pasa nada jejeje jklsdjalkdsa
        //Funciona el logout para ambos servicios
/*
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

        */
    }

    public void ListarCategorias(){
        dbOruro = FirebaseDatabase.getInstance().getReference().child("Categorias").child("01");
        dbOruro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNombre.setText((dataSnapshot.child("Nombre").getValue().toString()));
                tvDescripcion.setText((dataSnapshot.child("Descripcion").getValue().toString()));
                Glide.with(MainActivity.this).load(dataSnapshot.child("FotoUrl").getValue().toString()).into(fotoCategoria);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }


    private void goLoginScreen() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

