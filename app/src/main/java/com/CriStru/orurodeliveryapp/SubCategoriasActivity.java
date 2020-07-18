package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubCategoriasActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDataBase;
    private TextView tvNombreCategoria;
    private String idCategoria;
    private Bundle extras;
    private String nombreCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorias);
        setUpView();
        getItems();
    }

    private void setUpView() {
        mRecyclerView =findViewById(R.id.recyclerSubCategorias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mDataBase=FirebaseDatabase.getInstance().getReference();
        tvNombreCategoria=findViewById(R.id.tvNombreCategoriaSub);

        extras=getIntent().getExtras();
        if (extras.getString("idCategoria")!=null){
            idCategoria=extras.getString("idCategoria");
            Log.d("idCategoria",idCategoria);
        }
    }

    private void getItems(){
        mDataBase.child("Categorias").child(idCategoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    nombreCategoria=dataSnapshot.child("Nombre").getValue().toString();
                    Log.d("idCategoria",idCategoria);
                    tvNombreCategoria.setText(nombreCategoria);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}