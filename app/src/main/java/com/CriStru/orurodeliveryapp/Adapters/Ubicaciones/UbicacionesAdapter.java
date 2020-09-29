package com.CriStru.orurodeliveryapp.Adapters.Ubicaciones;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.CriStru.orurodeliveryapp.Adapters.Carrito.CarritoAdapter;
import com.CriStru.orurodeliveryapp.FormPedidoActivity;
import com.CriStru.orurodeliveryapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UbicacionesAdapter extends RecyclerView.Adapter<UbicacionesAdapter.ViewHolder> {

    public int Resource;
    public ArrayList<Ubicaciones> ubicacionesArrayList;
    public Context context;

    public UbicacionesAdapter(int resource, ArrayList<Ubicaciones> ubicacionesArrayList, Context context) {
        Resource = resource;
        this.ubicacionesArrayList = ubicacionesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(Resource,null,false);
        return new UbicacionesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ubicaciones ubicaion = ubicacionesArrayList.get(position);
        holder.nombreUbicaciones_txt.setText(ubicaion.getNombreUbicacion());
        holder.idUbicacion_txt.setText(ubicaion.getIdUbicacion());
    }

    @Override
    public int getItemCount() {
        return ubicacionesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public CardView cardView;
        public TextView nombreUbicaciones_txt,idUbicacion_txt;
        public ImageView borrarUbicaciones_btn;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            nombreUbicaciones_txt = (TextView) view.findViewById(R.id.nombreUbicacion_txt);
            idUbicacion_txt = (TextView) view.findViewById(R.id.idUbicaciontxt);
            borrarUbicaciones_btn = (ImageView) view.findViewById(R.id.eliminar_ubicacion_btn);
            cardView = (CardView) view.findViewById(R.id.ubicaciones_cardxd);
            cardView.setOnClickListener(this);
            borrarUbicaciones_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ubicaciones_cardxd:
                    Intent intent =new Intent(context, FormPedidoActivity.class);
                    intent.putExtra("preciototal", "");
                    intent.putExtra("direccion", "");
                    intent.putExtra("idUbicacion",idUbicacion_txt.getText());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case R.id.eliminar_ubicacion_btn:
                            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                            mDatabaseReference.child("Ubicacion").child(idUbicacion_txt.getText().toString()).removeValue();
                    break;
            }
        }


    }

}
