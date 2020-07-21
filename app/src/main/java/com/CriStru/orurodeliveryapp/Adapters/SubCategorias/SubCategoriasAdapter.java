package com.CriStru.orurodeliveryapp.Adapters.SubCategorias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.Models.SubCategoria;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubCategoriasAdapter extends RecyclerView.Adapter<SubCategoriasAdapter.ViewHolder> {
    private int resource;
    private ArrayList<SubCategoria> subCategoriaArrayList;
    private Context context;

    public SubCategoriasAdapter(int resource, ArrayList<SubCategoria> subCategoriaArrayList, Context context) {
        this.resource = resource;
        this.subCategoriaArrayList = subCategoriaArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubCategoria subCategoria=subCategoriaArrayList.get(position);
        holder.tvNombre.setText(subCategoria.getNombre());
        holder.tvDescripcion.setText(subCategoria.getDescripcion());
        holder.tvIdSubCategoria.setText(subCategoria.getIdSubCategoria());
        Glide.with(context).load(subCategoria.getFotoUrl()).into(holder.imageViewSubCategoria);
    }

    @Override
    public int getItemCount() {
        return subCategoriaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre,tvDescripcion,tvIdSubCategoria;
        ImageView imageViewSubCategoria;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            this.tvNombre=(TextView) view.findViewById(R.id.tvNombreSubCategoria);
            this.tvDescripcion=(TextView) view.findViewById(R.id.tvDescripcionSubCategoria);
            this.tvIdSubCategoria=(TextView) view.findViewById(R.id.tvIdSubCategoria);
            this.imageViewSubCategoria=(ImageView) view.findViewById(R.id.imageViewSubCategoria);
        }
    }
}
