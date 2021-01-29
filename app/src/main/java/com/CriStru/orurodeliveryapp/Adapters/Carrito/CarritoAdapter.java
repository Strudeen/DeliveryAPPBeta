package com.CriStru.orurodeliveryapp.Adapters.Carrito;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Adapters.Productos.ProductosAdapter;
import com.CriStru.orurodeliveryapp.DataTransferInterface;
import com.CriStru.orurodeliveryapp.Models.Carrito;
import com.CriStru.orurodeliveryapp.Models.Producto;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    DataTransferInterface dtInterface;
    private ArrayList<Carrito> carritoArrayList;
    private Context context;
    private int resource;
    public float precioTotal = 0f;

    public CarritoAdapter(int resource,ArrayList<Carrito> carritoArrayList, Context context,DataTransferInterface dtInterface) {
        this.carritoArrayList = carritoArrayList;
        this.context = context;
        this.resource = resource;
        this.dtInterface = dtInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,null,false);
        return new CarritoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carrito carrito=carritoArrayList.get(position);
        holder.tvNombre.setText(carrito.getNombre());
        holder.tvStock.setText("Stock: "+String.valueOf(carrito.getStock()));
        holder.tvPrecio.setText(String.valueOf(carrito.getPrecio())+" Bs");
        holder.tvIdProducto.setText(carrito.getIdProducto());
        Glide.with(context).load(carrito.getFotoUrl()).into(holder.imageViewProducto);
        holder.tvMaxStock.setText("Cantidad Actual: "+carrito.getMaxStock());
       // holder.Bind(carrito,this);
    }

    @Override
    public int getItemCount() {
        return carritoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNombre,tvPrecio,tvIdProducto,tvStock,tvMaxStock;
        ImageView imageViewProducto;
        ImageView btnMas,btnMenos,btnEliminar;
        List<Carrito> carritos;
        Carrito producto;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            SugarContext.init(context);
            this.tvIdProducto=(TextView) view.findViewById(R.id.tvIdCarrito);
            this.tvNombre=(TextView) view.findViewById(R.id.tvNombreCarritoCard);
            this.tvPrecio=(TextView) view.findViewById(R.id.tvPrecioCarritoCard);
            this.tvStock=(TextView) view.findViewById(R.id.tvStockCarritoCard);
            this.imageViewProducto=(ImageView) view.findViewById(R.id.imageViewCarritoCard);
            this.tvMaxStock = (TextView) view.findViewById(R.id.tvMaxStockCarritoCard);
            this.btnMas = (ImageView) view.findViewById(R.id.incrementar_button);
            this.btnMenos = (ImageView) view.findViewById(R.id.decrementar_button);
            this.btnEliminar = (ImageView) view.findViewById(R.id.borrar_button);
            this.btnMas.setOnClickListener(this);
            this.btnMenos.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {

        }
/*
        public void Bind(final Carrito data,final CarritoAdapter adapter){
            precioTotal += data.getMaxStock()*data.getPrecio();

            if (precioTotal < 80){
                precioTotal += 5;
            }
            dtInterface.onSetValues(precioTotal);
            Bundle bundle = new Bundle();

            bundle.putFloat("Cantidad",precioTotal);
                Log.d("PrecioTotal: ", ""+precioTotal);

            this.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Carrito carrito = Carrito.findById(Carrito.class, data.getId());
                    carrito.delete();

                    List<Carrito> data = Carrito.listAll(Carrito.class);
                    adapter.set(data);
                }
            });
            this.btnMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    producto = Carrito.findById(Carrito.class,data.getId());
                    if (producto.getMaxStock()<producto.getStock()){
                        int stockActual=producto.getMaxStock();
                        producto.setMaxStock(stockActual+1);
                        producto.save();
                        List<Carrito> data = Carrito.listAll(Carrito.class);
                        adapter.set(data);
                    }else
                        Toast.makeText(context, "No puedes agregar más productos", Toast.LENGTH_SHORT).show();
                }
            });
            this.btnMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    producto = Carrito.findById(Carrito.class, data.getId());
                    if (producto.getMaxStock()-1>=1){
                        int stockActual = producto.getMaxStock();
                        producto.setMaxStock(stockActual-1);
                        producto.save();
                        List<Carrito> data = Carrito.listAll(Carrito.class);
                        adapter.set(data);
                    } else {
                        Toast.makeText(context, "La cantidad debe ser almenos 1", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/
    }

    private void set(List<Carrito> data) {
     //   this.carritoArrayList = (ArrayList<Carrito>) data;
        precioTotal = 0;

         Log.d("PrecioTotal2: ", ""+precioTotal);
        notifyDataSetChanged();
    }
}