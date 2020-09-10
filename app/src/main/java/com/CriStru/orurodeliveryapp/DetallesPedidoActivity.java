package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.Productos.ProductosAdapter;
import com.CriStru.orurodeliveryapp.Models.Producto;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetallesPedidoActivity extends AppCompatActivity implements OnMapReadyCallback {


    private TextView nombretxt, numerotxt, referenciatxt, direcciontxt, precioCobrartxt;
    private Button sendNotification_btn;
    private Bundle extras;
    private GoogleMap mMap;
    private DatabaseReference mDatabaseReference;
    private String idPedido = "";
    private ArrayList<Producto> productoArrayList = new ArrayList<>();
    private ProductosAdapter mAdapter;
    private RecyclerView recyclerView;
    private String idUbicacion = "";
    Double latitude = 0d;
    Double longitude = 0d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);
        nombretxt = findViewById(R.id.nombreUsuario_txt);
        numerotxt = findViewById(R.id.numeroRef_txt);
        referenciatxt = findViewById(R.id.referencia_txt);
        direcciontxt = findViewById(R.id.direccion_txt);
        precioCobrartxt = findViewById(R.id.precioCobrar_txt);
        //sendNotification_btn = findViewById(R.id.sendNotifyBtn);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarSubcat);
        setSupportActionBar(myToolbar);
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_back);
        menuIcon.setColorFilter(getResources().getColor(R.color.colorWhiter), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recycler_detallesPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        extras = getIntent().getExtras();
        if (extras.getString("IdPedido") != null) {
            idPedido = extras.getString("IdPedido");
            Log.d("IdPedido", idPedido);
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child("Pedidos").child(idPedido).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    idUbicacion = dataSnapshot.child("idUbicacion").getValue().toString();
                    precioCobrartxt.setText("Precio a Cobrar: " + dataSnapshot.child("Precio Total").getValue().toString());
                    Log.d("IDUBICACIOON", idUbicacion);
                    getUbicacion(idUbicacion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.child("Pedidos").child(idPedido).child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds:
                         dataSnapshot.getChildren()) {
                        mDatabaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds2:
                                        dataSnapshot.getChildren()) {
                                    Log.d("IDPRODUCTO", ""+ds.getKey()+ds2.getKey());
                                    if(ds.getKey().equals(ds2.getKey())){

                                        String SubCategoria=ds2.child("subCategoria").getValue().toString();
                                        String Nombre=ds2.child("nombre").getValue().toString();
                                        String Descripcion=ds2.child("descripcion").getValue().toString();
                                        String FotoUrl=ds2.child("fotoUrl").getValue().toString();
                                        int Stock=Integer.parseInt(ds.child("cantidad").getValue().toString());
                                        float precio=Float.parseFloat(ds2.child("precio").getValue().toString());
                                        String IdProducto=ds.getKey();
                                        productoArrayList.add(new Producto(SubCategoria,Nombre,Descripcion,FotoUrl,Stock,precio,IdProducto));
                                    }
                                }
                                mAdapter=new ProductosAdapter(R.layout.productos_card,productoArrayList,getApplicationContext());
                                recyclerView.setAdapter(mAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.d("IDPRODUCTOS", ""+ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detalles_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tomar_pedido:
                tomarPedido();
                return true;
            case R.id.completar_pedido:
                mDatabaseReference.child("Pedidos").child(idPedido).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("DLY").exists()){
                            mDatabaseReference.child("Pedidos").child(idPedido).child("estado").setValue(true);
                            Toast.makeText(DetallesPedidoActivity.this, "Pedido Completado!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(DetallesPedidoActivity.this, "El pedido no ha sido tomado", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.enviar_notificacion:
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                String token = sharedPref.getString("token", "0");
                sendNotifyToClient(token);
                Toast.makeText(this, "Haz enviado una notificación", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tomarPedido() {
        mDatabaseReference.child("Pedidos").child(idPedido).child("DLY").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(DetallesPedidoActivity.this, "El pedido ya ha sido tomado", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabaseReference.child("Pedidos").child(idPedido).child("DLY").setValue(user.getUid());
                    Toast.makeText(DetallesPedidoActivity.this, "Tomaste el pedido", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUbicacion(String idUbicacion){
        mDatabaseReference.child("Ubicacion").child(idUbicacion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("IDUBICACIOON", idUbicacion);
                    String latitudeS= dataSnapshot.child("latitude").getValue().toString();
                    String longitudeD=dataSnapshot.child("longitude").getValue().toString();
                    latitude =Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    longitude =Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                    LatLng clienteUbicacion = new LatLng(latitude, longitude);
                    Log.d("LATLONG",""+clienteUbicacion.latitude+","+clienteUbicacion.longitude +"LATLNGSTR"+latitudeS+","+longitudeD);
                    mMap.addMarker(new MarkerOptions().position(clienteUbicacion).title("Ubicación del Cliente"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(clienteUbicacion));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clienteUbicacion, 17.0f));
                    numerotxt.setText(dataSnapshot.child("celreferencia").getValue().toString());
                    referenciatxt.setText(dataSnapshot.child("referencia").getValue().toString());
                    direcciontxt.setText(dataSnapshot.child("direccion").getValue().toString());
                    String name = dataSnapshot.child("uid").getValue().toString();
                    mDatabaseReference.child("Usuario").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                nombretxt.setText(dataSnapshot.child("nombre").getValue().toString());
                                String token = dataSnapshot.child("token").getValue().toString();
                                SharedPreferences sharedPref = DetallesPedidoActivity.this.getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("token", token);
                                editor.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifyToClient(String token) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            json.put("to", token);
            JSONObject notify = new JSONObject();
            notify.put("titulo", "soy un titulo xD");
            notify.put("detalle", "tu pedido xD");

            json.put("data", notify);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json,null, null){
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAzRL7dmw:APA91bGbVxgBjhQOBzu1xVMJIaHd2yN8QY40R1yJPtDXJRXVdhhRiMjbSa93F49uLASIL7LHp4fI3nhsp2twP_CAJoOF0PWpg4PXLWzhJqWXfCNdlT-diohg5XCnV-iV3hrN97_mZfTZ");
                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}