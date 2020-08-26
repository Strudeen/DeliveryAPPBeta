package com.CriStru.orurodeliveryapp.Adapters.Scroll;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterScroll extends RecyclerView.Adapter<AdapterScroll.ViewHolder> {
    private int resource;
    ArrayList<Promociones> promociones;
    private Context context;

    public AdapterScroll(ArrayList<Promociones> promociones,int resource,Context context){
        this.promociones=promociones;
        this.resource=resource;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new AdapterScroll.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promociones promocion=promociones.get(position);
        Glide.with(context).load(promocion.getFotoUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return promociones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public ImageView imageView;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            this.view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            this.imageView = (ImageView) view.findViewById(R.id.image_scroll);
            //imageView.setMinimumWidth(pxToDp(1440));

        }
        public int pxToDp(int px) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
    }
}
