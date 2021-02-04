package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.Categorias.CategoriasAdapter;
import com.CriStru.orurodeliveryapp.Adapters.RegistroPedidos.RegistroPedidosAdapter;
import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.Models.Pedido;
import com.CriStru.orurodeliveryapp.Models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Registro_pedidos extends AppCompatActivity {


    Spinner sp_mes, sp_year;
    Button list_button;
    String mes="";
    String year="";
    TextView ganancia;
    ListView allpedidos_list;
    ArrayList<Pedido> listagenerica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pedidos);


        list_button = findViewById(R.id.list_timestamp_button);
        allpedidos_list = findViewById(R.id.allpedidos_list);
        sp_mes = findViewById(R.id.spidmes);
        sp_year = findViewById(R.id.spidyear);
        listagenerica = new ArrayList<>();
        ganancia = findViewById(R.id.tv_gananciaMes);

        List<String> dialogList = new ArrayList<>();

        dialogList.add("01");
        dialogList.add("02");
        dialogList.add("03");
        dialogList.add("04");
        dialogList.add("05");
        dialogList.add("06");
        dialogList.add("07");
        dialogList.add("08");
        dialogList.add("09");
        dialogList.add("10");
        dialogList.add("11");
        dialogList.add("12");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,dialogList);
        sp_mes.setAdapter(adapter);
        sp_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mes = dialogList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        List<String> dialogList2 = new ArrayList<>();

        dialogList2.add("2021");
        dialogList2.add("2022");
        dialogList2.add("2023");
        dialogList2.add("2024");
        dialogList2.add("2025");
        dialogList2.add("2026");
        dialogList2.add("2027");
        dialogList2.add("2028");
        dialogList2.add("2029");
        dialogList2.add("2030");
        dialogList2.add("2031");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,dialogList2);
        sp_year.setAdapter(adapter1);
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = dialogList2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPedidos();


            }
        });

    }

    public void getPedidos() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("Pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listagenerica.clear();
                if (dataSnapshot.exists()) {
                    ganancia.setText("0.0Bs");
                    try {
                        int i = 1;
                        float gananciaMes = 0.0f;

                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            if (ds.child("estado").exists() && ds.child("FechaPedido").exists()){
                                String gananciacortada[] = ds.child("Precio Total").getValue().toString().split(" ");

                                gananciaMes += Float.parseFloat(gananciacortada[0]);
                                long timestamp = Long.parseLong(ds.child("FechaPedido").getValue().toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                String dateStr = sdf.format(timestamp);
                                Pedido mPedido= new Pedido(ds.getKey() + " " + gananciaMes, "Pedido: "+ i + "\n" + "Completado el " + dateStr);
                                listagenerica.add(mPedido);
                                i++;

                                Log.d("timestamplong", "" + timestamp + " " + dateStr);
                            } else {
                                Toast.makeText(Registro_pedidos.this, "Registros Mostrados", Toast.LENGTH_SHORT).show();
                            }


                        }



                        for (int y = 0; y < listagenerica.size() ; y++){
                            Log.d("listagenerica", ""+ listagenerica.get(y));

                        }



                        RegistroPedidosAdapter adapter = new RegistroPedidosAdapter(Registro_pedidos.this, android.R.layout.simple_list_item_1, listarMes(year,mes, listagenerica));
                        allpedidos_list.setAdapter(adapter);
                        allpedidos_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Pedido mPedido = listagenerica.get(i);
                                String intentcortado[] = mPedido.getId().split(" ");
                                Intent intent = new Intent(Registro_pedidos.this, DetallesPedidoActivity.class);
                                    intent.putExtra("IdPedido", intentcortado[0]);
                                    startActivity(intent);

                            }
                        });




                    } catch (NullPointerException e) {
                        Log.d("exceptionxd" , ""+e);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public ArrayList<Pedido> listarMes(String year, String mes, ArrayList<Pedido> listagenerica){
        ArrayList<Pedido> lista2 = new ArrayList<>();

        for (int i = 0; i < listagenerica.size(); i++){
            Log.d("listarmes", ""+ listagenerica.get(i));

            String ganancia2[] = listagenerica.get(i).getId().split(" ");
            String listagenerica1[] = listagenerica.get(i).getDly().split("/");
            Log.d("listarmes4", ""+ listagenerica1[1]);
            if (listagenerica1[1].equals(mes) && listagenerica1[2].equals(year)){
                ganancia.setText(ganancia2[1] + "Bs");
                lista2.add(listagenerica.get(i));
            }
            Log.d("listarmes3", ""+ listagenerica1[1]);
        }

        return lista2;
    }



}