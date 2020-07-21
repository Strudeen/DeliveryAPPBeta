package com.CriStru.orurodeliveryapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.Productos.ProductosAdapter;
import com.CriStru.orurodeliveryapp.Models.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosFragment extends Fragment {
    private ArrayList<Producto> productoArrayList;
    private ProductosAdapter mAdapter;
    private DatabaseReference mDataBase;
    private Context context;
    private RecyclerView recyclerView;
    private String idSubcategoria;
    public ProductosFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productoArrayList=new ArrayList<>();
        mDataBase= FirebaseDatabase.getInstance().getReference();
        if (getArguments() != null){
            idSubcategoria = getArguments().getString("IDSUBCATEGORIA");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_list, container, false);
        context = getActivity().getApplicationContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        LlenarDatos();
        Toast.makeText(context, idSubcategoria, Toast.LENGTH_SHORT).show();
        return view;
    }

    private void LlenarDatos() {
        mDataBase.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    productoArrayList.clear();

                    for (DataSnapshot ds:
                         dataSnapshot.getChildren()) {

                        if (ds.child("SubCategoria").getValue().toString().equals(idSubcategoria)){
                            String SubCategoria=ds.child("SubCategoria").getValue().toString();
                            String Nombre=ds.child("Nombre").getValue().toString();
                            String Descripcion=ds.child("Descripcion").getValue().toString();
                            String FotoUrl=ds.child("FotoUrl").getValue().toString();
                            int Stock=Integer.parseInt(ds.child("Stock").getValue().toString());
                            float precio=Float.parseFloat(ds.child("Precio").getValue().toString());
                            String IdProducto=ds.getKey();
                            productoArrayList.add(new Producto(SubCategoria,Nombre,Descripcion,FotoUrl,Stock,precio,IdProducto));
                        }
                    }
                    mAdapter=new ProductosAdapter(R.layout.productos_card,productoArrayList,context);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}