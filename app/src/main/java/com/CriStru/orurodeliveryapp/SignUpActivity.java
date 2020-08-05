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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail,etContraseña,etNombre,etApellido, etCel;
    Button btnRegistrarse;
    ProgressBar progressBarSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpView();
    }

    private void setUpView() {
        etEmail=findViewById(R.id.etEmailSignIn);
        etContraseña=findViewById(R.id.etContraseñaSignIn);
        etNombre=findViewById(R.id.etNombre);
        etApellido=findViewById(R.id.etApellido);
        etCel =findViewById(R.id.etCel);
        btnRegistrarse=findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        progressBarSignUp=findViewById(R.id.progress_barSignUp);
        progressBarSignUp.setVisibility(View.INVISIBLE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent main=new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(main);
        }
        else {

        }
    }
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

        String celular= etCel.getText().toString();
        String[] numcel = celular.split(" ");
        Toast.makeText(getApplicationContext(),numcel[0],Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(celular)){
            etCel.setError("Este campo es obligatorio");
            valid=false;
        }else {
            if (celular.length() < 8 || celular.length() > 8){
                etCel.setError("Inserte un número valido");
                valid=false;
            }
            else {
                etCel.setError(null);
            }
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
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(etNombre.getText().toString() +" "+ etApellido.getText().toString()).build();
                            user.updateProfile(profileUpdates);


                            progressBarSignUp.setVisibility(View.INVISIBLE);
                            Log.d("Success", "createUserWithEmail:success");
                            SaveDataUser(user);
                            Log.d("Success", "Save user data");
                            //sendEmailVerification(user);
                            Log.d("Success", "Email Verification");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Error de Autenticación",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressBarSignUp.setVisibility(View.INVISIBLE);
                        }

                        // ...
                    }
                });
    }

    private void SaveDataUser(final FirebaseUser currentUser){
        progressBarSignUp.setVisibility(View.VISIBLE);
        if (currentUser!=null){
            final String Uid=currentUser.getUid();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference tableUsuario=database.getReference().child("Usuario");
            tableUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Uid).exists()){
                        Toast.makeText(SignUpActivity.this,"El usuario ya esta registrado",Toast.LENGTH_SHORT).show();
                        progressBarSignUp.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Usuario usuario=new Usuario(etNombre.getText().toString(),etApellido.getText().toString(), etCel.getText().toString(),"USR");
                        tableUsuario.child(Uid).setValue(usuario);
                        updateUI(currentUser);
                        progressBarSignUp.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    //TODO verificacion email
    /*private void sendEmailVerification(final FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrarse:
                CreateAccount(etEmail.getText().toString(),etContraseña.getText().toString());
                break;
        }
    }
}