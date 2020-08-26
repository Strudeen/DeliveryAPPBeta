package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Carrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FormPedidoActivity extends AppCompatActivity {

    private Button buscarbtn ,confirmar, direccionesguardadas_btn;
    private Bundle localizacion;
    private TextView confirmaciontxt;
    private DatabaseReference mDatabaseReference;
    private Double latitude=0d, longitude=0d;
    private EditText direcciontxt, referenciadirtxt, numeroreftxt;
    private String preciototal="", nombreUbicacion="";
    SharedPreferences.Editor myEditor;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pedido);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();

        direccionesguardadas_btn = findViewById(R.id.direcciones_btn);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        confirmar = findViewById(R.id.confirmar_btn);
        direcciontxt = findViewById(R.id.direccion_txt);
        referenciadirtxt = findViewById(R.id.referencia_txt);
        numeroreftxt = findViewById(R.id.telefono_txt);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitle("Registrarse");
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_back);
        menuIcon.setColorFilter(getResources().getColor(R.color.colorWhiter), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        confirmaciontxt = findViewById(R.id.confirmaciontxt);
        buscarbtn = findViewById(R.id.buscar_btn);
        buscarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditor.putString("direccion", direcciontxt.getText().toString());
                myEditor.putString("referencia", referenciadirtxt.getText().toString());
                myEditor.putString("telefono", numeroreftxt.getText().toString());
                myEditor.commit();
                Intent intent = new Intent(FormPedidoActivity.this, MapsPedidoActivity.class);
                startActivity(intent);
            }
        });

        direccionesguardadas_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditor.putString("direccion", direcciontxt.getText().toString());
                myEditor.putString("referencia", referenciadirtxt.getText().toString());
                myEditor.putString("telefono", numeroreftxt.getText().toString());
                myEditor.commit();
                Intent intent = new Intent(FormPedidoActivity.this, UbicacionesSavedActivity.class);
                startActivity(intent);
            }
        });
        direcciontxt.setText(sharedPref.getString("direccion", ""));
        referenciadirtxt.setText(sharedPref.getString("referencia", ""));
        numeroreftxt.setText(sharedPref.getString("telefono", ""));

        localizacion = getIntent().getExtras();
        if (!localizacion.getString("preciototal").equals("")){
            preciototal = localizacion.getString("preciototal");
            myEditor.putString("PrecioTotal", preciototal);
            myEditor.commit();
        }
        Log.d("precioTotal",sharedPref.getString("PrecioTotal","0"));

        if (!localizacion.getString("direccion").equals("") || !localizacion.getString("idUbicacion").equals("")) {
            Log.d("idUbicacion",localizacion.getString("idUbicacion"));
            if (!localizacion.getString("idUbicacion").equals("")){

                confirmaciontxt.setText("Ubicación agregada correctamente!");
                confirmaciontxt.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_success, 0);
                confirmaciontxt.setTextColor(getResources().getColor(R.color.success));
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmarPedido();
                    }
                });
            }
            else {
                String[] ubicacion = localizacion.getString("direccion").split(",");
                latitude = Double.parseDouble(ubicacion[0]);
                longitude = Double.parseDouble(ubicacion[1]);
                nombreUbicacion = ubicacion[2];
                Log.d("ubicacion", ""+latitude + "," + longitude);
                confirmaciontxt.setText("Ubicación agregada correctamente!");
                confirmaciontxt.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_success, 0);
                confirmaciontxt.setTextColor(getResources().getColor(R.color.success));
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmarPedido();
                    }
                });
            }
        }

        else {
            confirmaciontxt.setText("Hace falta una ubicación!");
            confirmaciontxt.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_error, 0);
            confirmaciontxt.setTextColor(getResources().getColor(R.color.failure));
        }


    }

    public void confirmarPedido(){
        Log.d("precioTotal",sharedPref.getString("PrecioTotal","0"));
        String idPedido="";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<Carrito> carritos = Carrito.listAll(Carrito.class);
        String idUbicacion = "";
        if (localizacion.getString("idUbicacion").equals("")){
            idUbicacion = mDatabaseReference.child("Ubicacion").push().getKey();
            mDatabaseReference.child("Ubicacion").child(idUbicacion).child("uid").setValue(user.getUid());
            mDatabaseReference.child("Ubicacion").child(idUbicacion).child("nombre").setValue(nombreUbicacion);
            mDatabaseReference.child("Ubicacion").child(idUbicacion).child("latitude").setValue(latitude);
            mDatabaseReference.child("Ubicacion").child(idUbicacion).child("longitude").setValue(longitude);
        }
        else {
            idUbicacion = localizacion.getString("idUbicacion");
            confirmaciontxt.setText("Ubicación agregada correctamente!");
        }
        idPedido = mDatabaseReference.child("Pedidos").push().getKey();
        for (int i=0;i<carritos.size();i++){
            Carrito carrito = carritos.get(i);
            mDatabaseReference.child("Pedidos").child(idPedido).child(carrito.getIdProducto()).setValue(carrito);
        }
        preciototal = sharedPref.getString("PrecioTotal","0");
        Log.d("precioTotal",preciototal);
        mDatabaseReference.child("Pedidos").child(idPedido).child("Precio Total").setValue(preciototal);
        mDatabaseReference.child("Pedidos").child(idPedido).child("Direccion").setValue(direcciontxt.getText().toString());
        mDatabaseReference.child("Pedidos").child(idPedido).child("Referencia").setValue(referenciadirtxt.getText().toString());
        mDatabaseReference.child("Pedidos").child(idPedido).child("NumeroRef").setValue(numeroreftxt.getText().toString());
        mDatabaseReference.child("Pedidos").child(idPedido).child("idUbicacion").setValue(idUbicacion);
        Toast.makeText(this, "Pedido en Proceso...", Toast.LENGTH_SHORT).show();

    }
}