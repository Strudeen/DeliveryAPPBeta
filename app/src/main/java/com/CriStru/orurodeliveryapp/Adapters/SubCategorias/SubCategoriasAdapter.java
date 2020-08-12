package com.CriStru.orurodeliveryapp.Adapters.SubCategorias;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.CriStru.orurodeliveryapp.UI.CategoriasDialogActivity;
import com.CriStru.orurodeliveryapp.UI.SubCategoriasDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNombre,tvDescripcion,tvIdSubCategoria;
        ImageView imageViewSubCategoria,editSubCategoria;
        public DatabaseReference mDatabase;
        public FirebaseAuth mAuth;
        public FirebaseUser mUser;
        public String tipo ="";



        View view;
        public ViewHolder(View view){
            super(view);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            this.view=view;
            this.tvNombre=(TextView) view.findViewById(R.id.tvNombreSubCategoria);
            this.tvDescripcion=(TextView) view.findViewById(R.id.tvDescripcionSubCategoria);
            this.tvIdSubCategoria=(TextView) view.findViewById(R.id.tvIdSubCategoria);
            this.imageViewSubCategoria=(ImageView) view.findViewById(R.id.imageViewSubCategoria);
            this.editSubCategoria=(ImageView) view.findViewById(R.id.editSubCategoria);
            this.editSubCategoria.setOnClickListener(this);

            mDatabase.child("Usuario").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()){
                     tipo = dataSnapshot.child("tipo").getValue().toString();
                     if (tipo.equals("ADM")){
                         Log.d("TIPO", tipo);
                         editSubCategoria.setVisibility(View.VISIBLE);
                        editSubCategoria.setEnabled(true);
                         editSubCategoria.setClickable(true);
                     }
                     else if (tipo.equals("USR")){
                         Log.d("Tipo", tipo);
                         editSubCategoria.setVisibility(View.GONE);
                         editSubCategoria.setEnabled(false);
                         editSubCategoria.setClickable(false);
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
                case R.id.editSubCategoria:
                    Intent intent = new Intent(context, SubCategoriasDialog.class);
                    intent.putExtra("idSubCategoriaSubD",tvIdSubCategoria.getText().toString());
                    intent.putExtra("idCategoriaSubD","");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
