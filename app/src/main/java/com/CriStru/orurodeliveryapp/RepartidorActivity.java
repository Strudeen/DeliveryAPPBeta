package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RepartidorActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Bundle bundle;
    Fragment fragment;
    Button  logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);

        bundle = new Bundle();
        bundle.putBoolean("PEDIDOSESTADO",false);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fragment=new PedidosFragment();
      //  logout = findViewById(R.id.btnLogout);
        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,fragment).addToBackStack(null).commit();


        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        fragment=new PedidosFragment();
                        bundle.putBoolean("PEDIDOSESTADO",false);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,fragment).addToBackStack(null).commit();
                        break;
                    case 1:
                        fragment=new PedidosFragment();
                        bundle.putBoolean("PEDIDOSESTADO",true);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,fragment).addToBackStack(null).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_repartidor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_repartidorbtn:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
                return true;
            default:
                return false;
        }

    }

    private void goLoginScreen() {
        Intent intent = new Intent(RepartidorActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bundle = new Bundle();
        bundle.putBoolean("PEDIDOSESTADO",false);
        fragment=new PedidosFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,fragment).addToBackStack(null).commit();
    }
}