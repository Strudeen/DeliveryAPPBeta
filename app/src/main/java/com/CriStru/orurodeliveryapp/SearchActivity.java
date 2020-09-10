package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private ArrayList<Producto> list;
    private RecyclerView rv;
    private SearchView searchView;
    private ProductosAdapter adapterProducto;
    private ArrayList<Producto>milista;
    private LinearLayoutManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ref = FirebaseDatabase.getInstance().getReference().child("Producto");
        rv = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list = new ArrayList<>();
        searchView.setQuery("", true);
        //adapterProducto = new ProductosAdapter(R.layout.productos_card,list,getApplicationContext());
        //rv.setAdapter(adapterProducto);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Producto producto = snapshot.getValue(Producto.class);
                        Producto producto = new Producto(snapshot.child("nombre").getValue().toString(),snapshot.child("descripcion").getValue().toString(),snapshot.child("fotoUrl").getValue().toString(),Integer.parseInt(snapshot.child("stock").getValue().toString()),Float.parseFloat(snapshot.child("precio").getValue().toString()),snapshot.getKey());
                        list.add(producto);
                    }
//                    adapterProducto.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("StringBus",query);
                if (!query.equals("") && query != "" && query !=null){
                    buscar(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

    }

    private void buscar(String s){
        if (!s.isEmpty()){
            milista = new ArrayList<>();
            milista.clear();
            for (Producto obj : list) {
                if (obj.getNombre().toLowerCase().contains(s.toLowerCase())){
                    milista.add(obj);
                    Log.d("IDProducto"," "+milista.get(0).getIdProducto());
                }
            }
            ProductosAdapter productosAdapter = new ProductosAdapter(R.layout.productos_card,milista,getApplicationContext());
            rv.setAdapter(productosAdapter);
            ItemClickSupportProductos.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    TextView idProducto = (TextView) v.findViewById(R.id.tvIdProducto);
                    Intent intent = new Intent(SearchActivity.this, ProductosActivity.class);
                    intent.putExtra("idProducto", idProducto.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}