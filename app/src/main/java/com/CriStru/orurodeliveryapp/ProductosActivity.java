package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductosActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvStock, tvPrecio, tvDescripcion;
    private Button addToShopbtn;
    private ImageView photoProduct;
    private DatabaseReference mDatabaseReference;
    private String idProducto;
    private Bundle extras,idCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        setupView();
        callData();
    }

    public void setupView(){
        tvNombre = findViewById(R.id.textviewNombre);
        tvCategoria = findViewById(R.id.textViewCategoria);
        tvStock = findViewById(R.id.textViewStock);
        tvPrecio = findViewById(R.id.textViewPrecio);
        tvDescripcion = findViewById(R.id.textViewDescripcion);
        addToShopbtn = findViewById(R.id.btnAñadirAlCarrito);
        photoProduct = findViewById(R.id.imageViewProductos);
        idCategoria = new Bundle();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        extras=getIntent().getExtras();
        if (extras.getString("idProducto")!=null){
            idProducto=extras.getString("idProducto");
        }
    }

    public void callData() {
        mDatabaseReference.child("Producto").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    tvNombre.setText(dataSnapshot.child("nombre").getValue().toString());
                    tvDescripcion.setText(dataSnapshot.child("descripcion").getValue().toString());
                    tvPrecio.setText(Float.parseFloat(dataSnapshot.child("precio").getValue().toString()) + " Bs");
                    tvStock.setText("Stock: " + Integer.parseInt(dataSnapshot.child("stock").getValue().toString()));
                    Glide.with(ProductosActivity.this).load(dataSnapshot.child("fotoUrl").getValue().toString()).into(photoProduct);
                    String id=dataSnapshot.child("subCategoria").getValue().toString();
                    mDatabaseReference.child("SubCategorias").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                String nombre=dataSnapshot.child("nombre").getValue().toString();
                                tvCategoria.setText(nombre);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}