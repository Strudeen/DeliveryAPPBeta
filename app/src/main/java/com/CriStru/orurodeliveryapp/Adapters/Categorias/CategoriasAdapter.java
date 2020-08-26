package com.CriStru.orurodeliveryapp.Adapters.Categorias;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.UI.CategoriasDialogActivity;
import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        holder.idCategorias.setText(categoria.getCategoria_id());
        Glide.with(context).load(categoria.getFotoUrl()).into(holder.imageViewCategorias);
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewNombreCategorias, textViewDescripcionCategorias, idCategorias;
        private ImageView imageViewCategorias,editCategoriaCard;
        public View view;
        public DatabaseReference mDatabase;
        public FirebaseAuth mAuth;
        public FirebaseUser mUser;
        public CardView mCardview;
        private String tipo="";

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewNombreCategorias = (TextView) view.findViewById(R.id.textViewNombreCategoria);
            this.textViewDescripcionCategorias = (TextView) view.findViewById(R.id.textViewDescripcionCategoria);
            this.imageViewCategorias = (ImageView) view.findViewById(R.id.imageViewCategorias);
            this.idCategorias = (TextView) view.findViewById(R.id.idCategorias);
            this.editCategoriaCard=(ImageView) view.findViewById(R.id.editCategoriaCard);
            this.mCardview = (CardView) view.findViewById(R.id.CardViewCategorias);
            this.imageViewCategorias.setMinimumWidth(pxToDp(644));

            this.editCategoriaCard.setOnClickListener(this);
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuario");
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mDatabase.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        tipo=dataSnapshot.child("tipo").getValue().toString();
                        Log.d("TIPO",tipo);
                        if (tipo.equals("USR")){
                            Log.d("TIPO",tipo);
                            editCategoriaCard.setVisibility(View.GONE);
                            editCategoriaCard.setEnabled(false);
                            editCategoriaCard.setClickable(false);
                        }
                        else if (tipo.equals("ADM")){
                            editCategoriaCard.setVisibility(View.VISIBLE);
                            editCategoriaCard.setEnabled(true);
                            editCategoriaCard.setClickable(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editCategoriaCard:
                    Intent intent = new Intent(context, CategoriasDialogActivity.class);
                    intent.putExtra("idCategoriaDialog",idCategorias.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        }
        public int pxToDp(int px) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
    }
}
