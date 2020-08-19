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

    private Button buscarbtn ,confirmar;
    private Bundle localizacion;
    private TextView confirmaciontxt;
    private DatabaseReference mDatabaseReference;
    private EditText direcciontxt, referenciadirtxt, numeroreftxt;
    private String preciototal="";
    SharedPreferences.Editor myEditor;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pedido);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();

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

        if (!localizacion.getString("direccion").equals("")) {
            String[] ubicacion = localizacion.getString("direccion").split(",");
            Double latitude = Double.parseDouble(ubicacion[0]);
            Double longitude = Double.parseDouble(ubicacion[1]);
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

        else {
            confirmaciontxt.setText("Hace falta una ubicación!");
            confirmaciontxt.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_error, 0);
            confirmaciontxt.setTextColor(getResources().getColor(R.color.failure));
        }


    }

    public void confirmarPedido(){
        Log.d("precioTotal",sharedPref.getString("PrecioTotal","0"));
        String idPedido="";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Pedidos");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<Carrito> carritos = Carrito.listAll(Carrito.class);
        idPedido = mDatabaseReference.push().getKey();
        for (int i=0;i<carritos.size();i++){
            Carrito carrito = carritos.get(i);
            mDatabaseReference.child(idPedido).child(carrito.getIdProducto()).setValue(carrito);
        }
        preciototal = sharedPref.getString("PrecioTotal","0");
        Log.d("precioTotal",preciototal);
        mDatabaseReference.child(idPedido).child("Precio Total").setValue(preciototal);
        mDatabaseReference.child(idPedido).child("Direccion").setValue(direcciontxt.getText().toString());
        mDatabaseReference.child(idPedido).child("Referencia").setValue(referenciadirtxt.getText().toString());
        mDatabaseReference.child(idPedido).child("NumeroRef").setValue(numeroreftxt.getText().toString());
        Toast.makeText(this, "Pedido en Proceso...", Toast.LENGTH_SHORT).show();

    }
}