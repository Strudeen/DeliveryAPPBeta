package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.CriStru.orurodeliveryapp.Adapters.SubCategorias.ItemClickSupportSubCategorias;
import com.CriStru.orurodeliveryapp.Adapters.SubCategorias.SubCategoriasAdapter;
import com.CriStru.orurodeliveryapp.Models.SubCategoria;
import com.CriStru.orurodeliveryapp.UI.ProductosDialogActivity;
import com.CriStru.orurodeliveryapp.UI.SubCategoriasDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubCategoriasActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDataBase;
    private TextView tvNombreCategoria, tvEmpezar;
    private String idCategoria;
    private Bundle extras;
    private SubCategoriasAdapter mAdapter;
    private String nombreCategoria;
    private FirebaseAuth mAuth;
    private ArrayList<SubCategoria> subCategoriaArrayList = new ArrayList<>();
    private FloatingActionButton mShopAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorias);
        setUpView();
        getNombreCategoria();
        getSubCategorias();

        mShopAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubCategoriasActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpView() {
        mRecyclerView =findViewById(R.id.recyclerSubCategorias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mDataBase=FirebaseDatabase.getInstance().getReference();
        tvNombreCategoria=findViewById(R.id.tvNombreCategoriaSub);
        extras=getIntent().getExtras();
        tvEmpezar = findViewById(R.id.textviewEmpezar);
        mShopAction = findViewById(R.id.shopFloating_Button3);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarSubcat);
        setSupportActionBar(myToolbar);

        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_back);
        menuIcon.setColorFilter(getResources().getColor(R.color.colorWhiter), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitle("Subcategorias");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (extras.getString("idCategoria")!=null){
            idCategoria=extras.getString("idCategoria");
            Log.d("idCategoria",idCategoria);
        }
        ItemClickSupportSubCategorias.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupportSubCategorias.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                TextView textViewid=(TextView) v.findViewById(R.id.tvIdSubCategoria);
                String id=textViewid.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("IDSUBCATEGORIA",id);
                Fragment fragment=new ProductosFragment();
                fragment.setArguments(bundle);
                tvEmpezar.setVisibility(View.GONE);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().add(R.id.FragmentHolder,fragment).addToBackStack(null).commit();
            }
        });
    }

    private void getNombreCategoria(){
        mDataBase.child("Categorias").child(idCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
//                    nombreCategoria=dataSnapshot.child("nombre").getValue().toString();
                    Log.d("idCategoria",idCategoria);
               //     tvNombreCategoria.setText(nombreCategoria);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getSubCategorias(){
        mDataBase.child("SubCategorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    subCategoriaArrayList.clear();
                    for (DataSnapshot ds:
                            dataSnapshot.getChildren()){
                        if (ds.child("categoria").getValue().toString().equals(idCategoria)) {
                            String Nombre=ds.child("nombre").getValue().toString();
                            String Descripcion=ds.child("descripcion").getValue().toString();
                            String FotoUrl=ds.child("fotoUrl").getValue().toString();
                            String Categoria=ds.child("categoria").getValue().toString();
                            String id =ds.getKey();
                            subCategoriaArrayList.add(new SubCategoria(Categoria,Nombre,Descripcion,FotoUrl,id));
                        }
                    }
                    mAdapter = new SubCategoriasAdapter(R.layout.subcategorias_card,subCategoriaArrayList,getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.add);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDataBase.child("Usuario").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("tipo").getValue().toString().equals("ADM")){
                        getMenuInflater().inflate(R.menu.menu_icon_subcat, menu);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAñadirSubCategoria:
                Intent intent = new Intent(SubCategoriasActivity.this, SubCategoriasDialog.class);
                intent.putExtra("idSubCategoriaSubD", "");
                intent.putExtra("idCategoriaSubD",idCategoria);
                startActivity(intent);
                return true;
            case R.id.itemAñadirProducto:
                Intent intent2 = new Intent(SubCategoriasActivity.this, ProductosDialogActivity.class);
                intent2.putExtra("idCategoriaSubD",idCategoria);
                intent2.putExtra("idProductoD","");
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}