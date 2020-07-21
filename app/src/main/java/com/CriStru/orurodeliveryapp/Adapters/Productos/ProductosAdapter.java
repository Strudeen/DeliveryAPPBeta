package com.CriStru.orurodeliveryapp.Adapters.Productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Models.Producto;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {
    private ArrayList<Producto> productoArrayList;
    private Context context;
    private int resource;

    public ProductosAdapter(int resource,ArrayList<Producto> productoArrayList, Context context) {
        this.productoArrayList = productoArrayList;
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto=productoArrayList.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvStock.setText(String.valueOf(producto.getStock()));
        holder.tvPrecio.setText(String.valueOf(producto.getPrecio()));
        holder.tvIdProducto.setText(producto.getIdProducto());
        Glide.with(context).load(producto.getFotoUrl()).into(holder.imageViewProducto);
    }

    @Override
    public int getItemCount() {
        return productoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre,tvPrecio,tvIdProducto,tvStock;
        ImageView imageViewProducto;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            this.tvIdProducto=(TextView) view.findViewById(R.id.tvIdProducto);
            this.tvNombre=(TextView) view.findViewById(R.id.tvNombreProductoCard);
            this.tvPrecio=(TextView) view.findViewById(R.id.tvPrecioCard);
            this.tvStock=(TextView) view.findViewById(R.id.tvStockCard);
            this.imageViewProducto=(ImageView) view.findViewById(R.id.imageViewProductosCard);
        }
    }
}
