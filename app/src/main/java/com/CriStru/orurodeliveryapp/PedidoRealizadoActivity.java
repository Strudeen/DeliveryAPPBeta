package com.CriStru.orurodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PedidoRealizadoActivity extends AppCompatActivity {

    private TextView tvAgradeciemnto;
    private String[] name;
    private ImageView buttonConfirmar, buttonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_realizado);
        tvAgradeciemnto = findViewById(R.id.tvAgradecimiento);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidoRealizadoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        if (user != null) {
            if (user.getDisplayName() != null) {
                name = user.getDisplayName().split(" ");
                tvAgradeciemnto.setText("¡Gracias por tu preferencia " + name[0] + "!");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(PedidoRealizadoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(PedidoRealizadoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}