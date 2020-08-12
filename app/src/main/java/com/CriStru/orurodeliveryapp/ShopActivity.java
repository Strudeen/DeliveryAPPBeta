package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.Carrito.CarritoAdapter;
import com.CriStru.orurodeliveryapp.Adapters.Categorias.CategoriasAdapter;
import com.CriStru.orurodeliveryapp.Models.Carrito;
import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.facebook.login.widget.LoginButton;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements DataTransferInterface {

    private CarritoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView tvprecioTotal;
    private ArrayList<Carrito> categoriaList;
    private Float precioTotal=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_shop);
        mRecyclerView = findViewById(R.id.recyclerCarrito);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Carrito> carritos = Carrito.listAll(Carrito.class);
        Log.d("PrecioTotal",""+precioTotal);
        categoriaList = new ArrayList<>(carritos);
        mAdapter = new CarritoAdapter(R.layout.carrito_card,categoriaList,ShopActivity.this,this);
        precioTotal=mAdapter.precioTotal;
        Log.d("PrecioTotal",""+precioTotal);
        Toast.makeText(this, ""+precioTotal, Toast.LENGTH_SHORT).show();
        mRecyclerView.setAdapter(mAdapter);
        tvprecioTotal = findViewById(R.id.tvPrecioTotal);
    }

    @Override
    public void onSetValues(float al) {
        tvprecioTotal.setText(""+al);
    }
}