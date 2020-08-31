package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PedidoRealizadoActivity extends AppCompatActivity {

    private TextView tvAgradeciemnto;
    private String[] name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_realizado);
        tvAgradeciemnto = findViewById(R.id.tvAgradecimiento);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null) {
                name = user.getDisplayName().split(" ");
                tvAgradeciemnto.setText("¡Gracias por tu Preferencia " + name[0] + "!");
            }
        }
    }
}