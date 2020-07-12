package com.CriStru.orurodeliveryapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Categoria> categoriaList;
    private Context context;

    public CategoriasAdapter(ArrayList<Categoria> categoriaList,int resource,Context context){
        this.categoriaList=categoriaList;
        this.resource=resource;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //obtener solo un item de la base de datos
        Categoria categoria=categoriaList.get(position);
        holder.textViewNombreCategorias.setText(categoria.getNombre());
        holder.textViewDescripcionCategorias.setText(categoria.getDescripcion());
        Glide.with(context).load(categoria.getFotoUrl()).into(holder.imageViewCategorias);
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewNombreCategorias,textViewDescripcionCategorias;
        private ImageView imageViewCategorias;
        public View view;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            this.textViewNombreCategorias=(TextView)view.findViewById(R.id.textViewNombreCategoria);
            this.textViewDescripcionCategorias=(TextView) view.findViewById(R.id.textViewDescripcionCategoria);
            this.imageViewCategorias=(ImageView)view.findViewById(R.id.imageViewCategorias);
        }
    }



}
