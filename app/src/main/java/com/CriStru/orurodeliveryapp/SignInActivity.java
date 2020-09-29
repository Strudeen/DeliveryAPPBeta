package com.CriStru.orurodeliveryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputConnectionCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.SigningInfo;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.CriStru.orurodeliveryapp.Models.Usuario;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    Button btningresar,btnRegistrarse, signInButton, loginButton ;
    EditText etEmail,etContraseña;
    private TextView textViewUser;
    private String emailx = "";
    private  Button facebookLogin;

    private static final int RC_SIGN_IN = 123;
   // private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private LoginManager loginManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthenticaion";
    private ProgressBar progressBar,progressBarUpdate;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setContentView(R.layout.activity_sign_in);
        setUpView();
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        Log.d("prueba", "xd");

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this,Arrays.asList("email", "public_profile"));
                Log.d("onclick", "facebook:click:");
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("djvbdfjbv", "facebook:onSuccess:" + loginResult);
                        handleTokenFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignInActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        Log.d("cancelfb", "facebook:cancel:");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SignInActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("errorfb", "facebook:error:" + error);
                    }
                });

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (accessTokenTracker == null) {
                    mAuth.signOut();
                }
            }
        };

    }
    //Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.d("enta",""+requestCode);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("FAiledAuht", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("userxd", "xd" + user.getUid());
                            DatabaseReference tableUsuario=FirebaseDatabase.getInstance().getReference().child("Usuario");
                            tableUsuario.child(user.getUid()).child("nombre").setValue(user.getDisplayName());
                            tableUsuario.child(user.getUid()).child("tipo").setValue("USR");
                            String fbemail = user.getEmail();
                            user.updateEmail(fbemail);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("errogoogl", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //Facebook
    private void handleTokenFacebook(AccessToken token) {

        Log.d("smdbcdbsc", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("smdbcdbsc", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference tableUsuario=FirebaseDatabase.getInstance().getReference().child("Usuario");
                            tableUsuario.child(user.getUid()).child("nombre").setValue(user.getDisplayName());
                            tableUsuario.child(user.getUid()).child("tipo").setValue("USR");
                            String fbemail = user.getEmail();
                            user.updateEmail(fbemail);
                            Log.d("emailfb", "xd"+  user.getEmail());
                            mDatabaseReference.child("Usuario").child(user.getUid()).child("token").setValue(Fcm.getToken(getApplicationContext()));
                            //System.out.println(profile.getProfilePictureUri(20,20)); System.out.println(profile.getLinkUri());
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("smdbcdbsc", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }


    private void updateUI(FirebaseUser currentUser) {
        progressBarUpdate.setVisibility(View.VISIBLE);

        if (currentUser != null){
            Log.d("namegogl", "xd"+currentUser.getDisplayName() + currentUser.getUid());
            mDatabaseReference.child("Usuario").child(currentUser.getUid()).child("token").setValue(Fcm.getToken(this));
            mDatabaseReference.child("Usuario").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.child("tipo").exists() && dataSnapshot.child("tipo").getValue().toString().equals("DLY")){
                            Intent intent2 = new Intent(SignInActivity.this, RepartidorActivity.class);
                            startActivity(intent2);
                        }
                        else {
                            Intent main=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(main);
                            progressBarUpdate.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            progressBarUpdate.setVisibility(View.GONE);
        }
    }
    private void setUpView() {
        btningresar=findViewById(R.id.btnIngresarSignIn);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        signInButton = findViewById(R.id.signin_button);
        facebookLogin = findViewById(R.id.login_button);
        etEmail=findViewById(R.id.etEmailSignIn);
        etContraseña=findViewById(R.id.etContraseñaSignIn);
        progressBar = findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        btnRegistrarse=findViewById(R.id.btnRegistrarse);
        progressBarUpdate=findViewById(R.id.progresUpdateUI);
        btnRegistrarse.setOnClickListener(this);
        btningresar.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }
    private void SignIn(String email,String password){
        if (!ValidateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(SignInActivity.this, "Datos Incorrectos",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIngresarSignIn:
                ValidateForm();
                SignIn(etEmail.getText().toString(),etContraseña.getText().toString());
                break;
            case R.id.signin_button:
                signIn();
                break;
            case R.id.btnRegistrarse:
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean ValidateForm() {
        boolean valid = true;
        String email=etEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            etEmail.setError("Este campo es obligatorio");
            valid=false;
        }else {
            etEmail.setError(null);
        }
        String password = etContraseña.getText().toString();
        if (TextUtils.isEmpty(email)){
            etContraseña.setError("Este campo es obligatorio");
            valid = false;
        }else {
            etEmail.setError(null);
        }
        return  valid;
    }

}