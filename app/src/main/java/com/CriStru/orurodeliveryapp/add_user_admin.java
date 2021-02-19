package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Usuario;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_user_admin extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etContraseña, etNombre, etApellido;
    Button btnRegistrarse;
    ProgressBar progressBarSignUp;
    private FirebaseAuth mAuth;
    private String option="";
    private String DisplayName = "";

    private Spinner mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_admin);
        setUpView();





    }

    private void setUpView() {
        etEmail = findViewById(R.id.etEmailSignIn);
        etContraseña = findViewById(R.id.etContraseñaSignIn);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);

        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressBarSignUp = findViewById(R.id.progress_barSignUp);
        progressBarSignUp.setVisibility(View.INVISIBLE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitle("Registrar Nuevos Usuarios");
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_back);
        menuIcon.setColorFilter(getResources().getColor(R.color.colorWhiter), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSpinner = findViewById(R.id.spinnerCategorias);

        List<String> dialogList = new ArrayList<>();

        dialogList.add("USR");
        dialogList.add("ADM");
        dialogList.add("DLY");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,dialogList);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option = dialogList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid().toString();
        Log.d("user1", uid);
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
/*
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent main=new Intent(add_user_admin.this,MainActivity.class);
            //main.putExtra("DisplayName",etNombre.getText().toString()+" "+etApellido.getText().toString());
            Log.d("Sucess",currentUser.getDisplayName());
            startActivity(main);
        }
        else {

        }
    }

 */
    public boolean ValidateForm(){
        boolean valid = true;

        String email=etEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            etEmail.setError("Este campo es obligatorio");
            valid=false;
        }else {
            etEmail.setError(null);
        }

        String contraseña=etContraseña.getText().toString();
        if (TextUtils.isEmpty(contraseña)){
            etContraseña.setError("Este campo es obligatorio");
            valid=false;
        }else {
            if (contraseña.length()<6){
                etContraseña.setError("La contraseña debe tener al menos 6 caracteres");
                valid=false;
            }
            else {
                etContraseña.setError(null);
            }
        }

        String nombre=etNombre.getText().toString();
        if (TextUtils.isEmpty(nombre)){
            etNombre.setError("Este campo es obligatorio");
            valid=false;
        }else {
            etNombre.setError(null);
        }

        String apellido=etApellido.getText().toString();
        if (TextUtils.isEmpty(apellido)){
            etApellido.setError("Este campo es obligatorio");
            valid=false;
        }else {
            etApellido.setError(null);
        }

        if (option.equals("")){
            valid=false;
        }
        return valid;
    }

    private void CreateAccount(String email,String password){
        if (!ValidateForm()) {
            return;
        }
        progressBarSignUp.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          FirebaseUser user = mAuth.getCurrentUser();
                          String user2 = user.getUid().toString();
                          Log.d("user2", user2);
                            Log.d("Success", "DisplayName"+user.getDisplayName());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(etNombre.getText().toString() +" "+ etApellido.getText().toString()).build();
                            user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("Success", "DisplayName"+user.getDisplayName());
                                    DisplayName = user.getDisplayName();
                                    Log.d("Success", "createUserWithEmail:success");

                                    SaveDataUser(user);
                                    Toast.makeText(add_user_admin.this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                                    //sendEmailVerification(user);
                                    Log.d("Success", "Email Verification");

                                   // updateUI(user);

                                    progressBarSignUp.setVisibility(View.INVISIBLE);

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(add_user_admin.this, "Error de Autenticación",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            progressBarSignUp.setVisibility(View.INVISIBLE);
                        }

                        // ...
                    }
                });
    }

    private void SaveDataUser(final FirebaseUser currentUser){
        if (currentUser!=null){
            final String Uid=currentUser.getUid();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference tableUsuario=database.getReference().child("Usuario");
            tableUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    if (dataSnapshot.child(Uid).exists()){
                        Toast.makeText(add_user_admin.this,"El usuario ya esta registrado",Toast.LENGTH_SHORT).show();
                        progressBarSignUp.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Usuario usuario=new Usuario(etNombre.getText().toString()+" "+etApellido.getText().toString(), option);
                        tableUsuario.child(Uid).setValue(usuario);
                        tableUsuario.child(Uid).child("token").setValue(Fcm.getToken(getApplicationContext()));

                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        goLoginScreen();


                        Log.d("Success", "Save user data");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(add_user_admin.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrarse:
                CreateAccount(etEmail.getText().toString(),etContraseña.getText().toString());
                break;
        }
    }


}