package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.CriStru.orurodeliveryapp.Adapters.Ubicaciones.Ubicaciones;
import com.CriStru.orurodeliveryapp.Adapters.Ubicaciones.UbicacionesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UbicacionesSavedActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;
    private UbicacionesAdapter ubicacionesAdapter;
    private ArrayList<Ubicaciones> arrayList = new ArrayList<>();
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicaciones_saved);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = findViewById(R.id.recycler_view_ubicaciones);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        radioGroup = findViewById(R.id.RadioGroupUbicaciones);
        LoadUbicacions();
    }

    private void LoadUbicacions() {
        mDatabaseReference.child("Ubicacion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds :
                            dataSnapshot.getChildren()) {
                        if (ds.child("uid").getValue().toString().equals(user.getUid())) {
                            String id = ds.getKey();
                            String nombre = ds.child("nombre").getValue().toString();
                            arrayList.add(new Ubicaciones(id, nombre));
                        }
                    }
                    ubicacionesAdapter = new UbicacionesAdapter(R.layout.ubicaciones_card, arrayList, getApplicationContext());
                    mRecyclerView.setAdapter(ubicacionesAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}