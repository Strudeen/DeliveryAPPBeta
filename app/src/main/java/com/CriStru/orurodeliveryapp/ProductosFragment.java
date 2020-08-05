package com.CriStru.orurodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.Categorias.ItemClickSupport;
import com.CriStru.orurodeliveryapp.Adapters.Productos.ItemClickSupportProductos;
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
        ItemClickSupportProductos.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                TextView idProducto = (TextView) v.findViewById(R.id.tvIdProducto);
                Intent intent = new Intent(context, ProductosActivity.class);
                intent.putExtra("idProducto", idProducto.getText().toString());
                startActivity(intent);
                Toast.makeText(context, idProducto.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

                        if (ds.child("subCategoria").getValue().toString().equals(idSubcategoria)){
                            String SubCategoria=ds.child("subCategoria").getValue().toString();
                            String Nombre=ds.child("nombre").getValue().toString();
                            String Descripcion=ds.child("descripcion").getValue().toString();
                            String FotoUrl=ds.child("fotoUrl").getValue().toString();
                            int Stock=Integer.parseInt(ds.child("stock").getValue().toString());
                            float precio=Float.parseFloat(ds.child("precio").getValue().toString());
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