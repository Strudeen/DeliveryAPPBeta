package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

import java.util.List;

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


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_back);
        menuIcon.setColorFilter(getResources().getColor(R.color.colorWhiter), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mShopAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductosActivity.this, ShopActivity.class);
                startActivity(intent);
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
                if (dataSnapshot.exists() && Integer.parseInt(dataSnapshot.child("stock").getValue().toString())>0){
                    List<Carrito> carritos=Carrito.listAll(Carrito.class);
                    boolean existe=false;
                    if (carritos.size()>0){
                        for (Carrito c:
                                carritos) {
                            if (c.getIdProducto().equals(idProducto)){
                                existe=true;
                            }
                        }
                        if (existe == false){
                            String nombre=dataSnapshot.child("nombre").getValue().toString();
                            String fotoUrl = dataSnapshot.child("fotoUrl").getValue().toString();
                            String Stock = dataSnapshot.child("stock").getValue().toString();
                            String precio = dataSnapshot.child("precio").getValue().toString();
                            String id = dataSnapshot.getKey();
                            carrito = new Carrito(nombre, fotoUrl, Integer.parseInt(Stock),  1, Float.parseFloat(precio), id);
                            carrito.save();
                            Toast.makeText(ProductosActivity.this, "¡Producto añadido al carrito!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ProductosActivity.this, "¡El producto ya se encuentra añadido!", Toast.LENGTH_SHORT).show();
                            addToShopbtn.setText("Añadido al Carrito!");
                            addToShopbtn.setTextColor(getResources().getColor(R.color.colorWhiter));
                            addToShopbtn.setBackgroundTintList(getResources().getColorStateList(R.color.mainColor));
                        }
                    }
                    else {
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
                else {
                    Toast.makeText(ProductosActivity.this, "Stock insuficiente", Toast.LENGTH_SHORT).show();
                    addToShopbtn.setText("Producto sin existencias :(");
                    addToShopbtn.setTextColor(getResources().getColor(R.color.colorWhiter));
                    addToShopbtn.setBackgroundTintList(getResources().getColorStateList(R.color.failure));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void callData() {
        mDatabaseReference.child("Producto").child(idProducto).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tvNombre.setText(dataSnapshot.child("nombre").getValue().toString());
                    tvDescripcion.setText(dataSnapshot.child("descripcion").getValue().toString());
                    String precio= "Precio Unidad    " + "Bs. " + Float.parseFloat(dataSnapshot.child("precio").getValue().toString());
                    tvPrecio.setText(precio);
                    int stock = Integer.parseInt(dataSnapshot.child("stock").getValue().toString());
                    if (stock > 0){
                        tvStock.setText("En Stock " + stock + " unid.");
                    }
                    else{
                        tvStock.setText("No Disponible");
                    }

                    Glide.with(getApplicationContext()).load(dataSnapshot.child("fotoUrl").getValue().toString()).into(photoProduct);
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