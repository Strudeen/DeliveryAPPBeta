package com.CriStru.orurodeliveryapp.Adapters.Productos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Models.Producto;
import com.CriStru.orurodeliveryapp.R;
import com.CriStru.orurodeliveryapp.UI.ProductosDialogActivity;
import com.CriStru.orurodeliveryapp.UI.SubCategoriasDialog;
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
        holder.tvStock.setText("Stock: "+String.valueOf(producto.getStock()));
        holder.tvPrecio.setText(String.valueOf(producto.getPrecio())+" Bs");
        holder.tvIdProducto.setText(producto.getIdProducto());
        Glide.with(context).load(producto.getFotoUrl()).into(holder.imageViewProducto);
    }

    @Override
    public int getItemCount() {
        return productoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNombre,tvPrecio,tvIdProducto,tvStock;
        ImageView imageViewProducto,editarProducto;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            this.tvIdProducto=(TextView) view.findViewById(R.id.tvIdProducto);
            this.tvNombre=(TextView) view.findViewById(R.id.tvNombreProductoCard);
            this.tvPrecio=(TextView) view.findViewById(R.id.tvPrecioCard);
            this.tvStock=(TextView) view.findViewById(R.id.tvStockCard);
            this.imageViewProducto=(ImageView) view.findViewById(R.id.imageViewProductosCard);
            this.editarProducto = (ImageView) view.findViewById(R.id.editarProducto);
            this.editarProducto.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editarProducto:
                    Intent intent = new Intent(context, ProductosDialogActivity.class);
                    intent.putExtra("idProductoD",tvIdProducto.getText().toString());
                    intent.putExtra("idCategoriaSubD","");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
