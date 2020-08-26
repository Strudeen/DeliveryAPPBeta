package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Carrito;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orm.SugarContext;

public class ProductosActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvStock, tvPrecio, tvDescripcion;
    private Button addToShopbtn;
    private ImageView photoProduct;
    private DatabaseReference mDatabaseReference;
    private String idProducto;
    private Bundle extras,idCategoria;
    private Carrito carrito;
    private FloatingActionButton mShopAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        SugarContext.init(this);
        setupView();
        callData();

        mShopAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductosActivity.this, ShopActivity.class);
                startActivity(intent);
                Toast.makeText(ProductosActivity.this, "Carritp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupView(){
        tvNombre = findViewById(R.id.textviewNombre);
        tvCategoria = findViewById(R.id.textViewCategoria);
        tvStock = findViewById(R.id.textViewStock);
        tvPrecio = findViewById(R.id.textViewPrecio);
        tvDescripcion = findViewById(R.id.textViewDescripcion);
        addToShopbtn = findViewById(R.id.btnAñadirAlCarrito);
        mShopAction = findViewById(R.id.shopFloating_Button3);
        addToShopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                añadirAlCarrito();
            }
        });
        photoProduct = findViewById(R.id.imageViewProductos);
        idCategoria = new Bundle();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        extras=getIntent().getExtras();
        if (extras.getString("idProducto")!=null){
            idProducto=extras.getString("idProducto");
        }
    }

    private void añadirAlCarrito() {
        mDatabaseReference.child("Producto").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String nombre=dataSnapshot.child("nombre").getValue().toString();
                    String fotoUrl = dataSnapshot.child("fotoUrl").getValue().toString();
                    String Stock = dataSnapshot.child("stock").getValue().toString();
                    String precio = dataSnapshot.child("precio").getValue().toString();
                    String id = dataSnapshot.getKey();
                    carrito = new Carrito(nombre, fotoUrl, Integer.parseInt(Stock),  1, Float.parseFloat(precio), id);
                    carrito.save();
                    Toast.makeText(ProductosActivity.this, "¡Producto añadido al carrito!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void callData() {
        mDatabaseReference.child("Producto").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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