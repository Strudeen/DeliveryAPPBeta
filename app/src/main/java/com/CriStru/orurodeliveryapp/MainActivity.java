package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.CategoriasAdapter;
import com.CriStru.orurodeliveryapp.Adapters.ItemClickSupport;
import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Button logout;
    private DatabaseReference dbOruro;
    private FirebaseAuth mAuth;
    private CategoriasAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Categoria> categoriaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView=findViewById(R.id.recyclerviewCategorias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbOruro=FirebaseDatabase.getInstance().getReference();

        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getApplicationContext(),"Posicion"+position,Toast.LENGTH_SHORT).show();
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
        //ListarCategorias();
        getCategoriasFromFirebase();


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

    /*public void ListarCategorias(){
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
    }*/

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
     public void getCategoriasFromFirebase(){
        dbOruro.child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    categoriaList.clear();
                    for (DataSnapshot ds:
                         dataSnapshot.getChildren()) {
                        String Nombre=ds.child("Nombre").getValue().toString();
                        String Descripcion=ds.child("Descripcion").getValue().toString();
                        String FotoUrl=ds.child("FotoUrl").getValue().toString();
                        categoriaList.add(new Categoria(Nombre,Descripcion,FotoUrl));
                    }
                    mAdapter=new CategoriasAdapter(categoriaList,R.layout.categorias_card,getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     }

    private void goLoginScreen() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

