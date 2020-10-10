package com.CriStru.orurodeliveryapp.Adapters.Scroll;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterScroll extends RecyclerView.Adapter<AdapterScroll.ViewHolder> {
    private int resource;
    ArrayList<Promociones> promociones;
    private Context context;
    private ViewPager2 viewPager2;
    private ImageView btn_borrar;
    private android.os.Handler handler = new Handler();

    public AdapterScroll(ArrayList<Promociones> promociones,int resource,Context context, ViewPager2 viewPager2){
        this.promociones=promociones;
        this.resource=resource;
        this.context=context;
        this.viewPager2 = viewPager2;
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
        holder.idPromocion_txt.setText(promocion.getId());
        if (position == promociones.size()-1){
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 3000);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return promociones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public ImageView imageView;
        public ImageView btn_borrar;
        public DatabaseReference mDatabase;
        public FirebaseAuth mAuth;
        public FirebaseUser mUser;
        public TextView idPromocion_txt;
        private String tipo="";

        public ViewHolder(View view){
            super(view);
            this.view=view;
            this.view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            this.imageView = (ImageView) view.findViewById(R.id.image_scroll);
            this.btn_borrar = (ImageView) view.findViewById(R.id.btn_borrar);
            this.idPromocion_txt = (TextView) view.findViewById(R.id.idPromocion);

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
                            btn_borrar.setVisibility(View.GONE);
                            btn_borrar.setClickable(false);
                            btn_borrar.setEnabled(false);

                        }
                        else if (tipo.equals("ADM")){
                            btn_borrar.setVisibility(View.VISIBLE);
                            btn_borrar.setClickable(true);
                            btn_borrar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            this.btn_borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReference.child("Promociones").child(idPromocion_txt.getText().toString()).removeValue();

                 }
            });


            //imageView.setMinimumWidth(pxToDp(1440));

        }
        public int pxToDp(int px) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(0);
        }
    };
}
