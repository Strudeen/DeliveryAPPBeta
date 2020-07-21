package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.CriStru.orurodeliveryapp.Adapters.SubCategorias.SubCategoriasAdapter;
import com.CriStru.orurodeliveryapp.Adapters.SubCategorias.ItemClickSupportSubCategorias;
import com.CriStru.orurodeliveryapp.Models.SubCategoria;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubCategoriasActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDataBase;
    private TextView tvNombreCategoria;
    private String idCategoria;
    private Bundle extras;
    private SubCategoriasAdapter mAdapter;
    private String nombreCategoria;
    private FirebaseAuth mAuth;
    private ArrayList<SubCategoria> subCategoriaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorias);
        setUpView();
        getNombreCategoria();
        getSubCategorias();
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
        ItemClickSupportSubCategorias.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupportSubCategorias.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                TextView textViewid=(TextView) v.findViewById(R.id.tvIdSubCategoria);
                String id=textViewid.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("IDSUBCATEGORIA",id);
                Fragment fragment=new ProductosFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().add(R.id.FragmentHolder,fragment).addToBackStack(null).commit();
            }
        });
    }

    private void getNombreCategoria(){
        mDataBase.child("Categorias").child(idCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void getSubCategorias(){
        mDataBase.child("SubCategorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    subCategoriaArrayList.clear();
                    for (DataSnapshot ds:
                            dataSnapshot.getChildren()){
                        if (ds.child("Categoria").getValue().toString().equals(idCategoria)) {
                            String Nombre=ds.child("Nombre").getValue().toString();
                            String Descripcion=ds.child("Descripcion").getValue().toString();
                            String FotoUrl=ds.child("FotoUrl").getValue().toString();
                            String Categoria=ds.child("Categoria").getValue().toString();
                            String id =ds.getKey();
                            subCategoriaArrayList.add(new SubCategoria(Categoria,Nombre,Descripcion,FotoUrl,id));
                        }
                    }
                    mAdapter = new SubCategoriasAdapter(R.layout.subcategorias_card,subCategoriaArrayList,getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}