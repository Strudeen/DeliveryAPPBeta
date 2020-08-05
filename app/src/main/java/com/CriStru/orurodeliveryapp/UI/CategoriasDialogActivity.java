package com.CriStru.orurodeliveryapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CategoriasDialogActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnCargarImagen,btnGuardar,btnCancelar;
    private EditText nombreCategoria,descripcionCategoria;
    private ImageView imageViewFoto;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Bundle extras;
    private String idCategoria,fotoUrl;
    private boolean changeFoto;
    private ProgressBar mProgresDialog;
    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_dialog);
        this.setFinishOnTouchOutside(false);
        setupView();
        configureServices();
    }

    private void setupView() {
        nombreCategoria=findViewById(R.id.etNombreCategoriasD);
        descripcionCategoria=findViewById(R.id.etDescripcionCategoriasD);
        btnCancelar=findViewById(R.id.btnCancelarCategoriasD);
        btnGuardar=findViewById(R.id.btnGuardarCategoriasD);
        btnCargarImagen=findViewById(R.id.btnCargarImagenCategoriasD);
        imageViewFoto=findViewById(R.id.imageViewFotoCategoriaD);
        mStorage=FirebaseStorage.getInstance().getReference("Categorias");
        mDatabase= FirebaseDatabase.getInstance().getReference("Categorias");
        mProgresDialog = findViewById(R.id.progressBar2);
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureServices(){
        extras=getIntent().getExtras();
        if (!extras.getString("idCategoriaDialog").equals("")){
            idCategoria=extras.getString("idCategoriaDialog");
            if (!idCategoria.equals("")){
                changeFoto = false;
                mDatabase.child(idCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            nombreCategoria.setText(dataSnapshot.child("nombre").getValue().toString());
                            descripcionCategoria.setText(dataSnapshot.child("descripcion").getValue().toString());
                            Glide.with(CategoriasDialogActivity.this).load(dataSnapshot.child("fotoUrl").getValue().toString()).into(imageViewFoto);
                            fotoUrl = dataSnapshot.child("fotoUrl").getValue().toString();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        //Log.d("idCategoriaDialog",idCategoria);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        mProgresDialog.setVisibility(View.VISIBLE);
        if (mImageUri != null){
            StorageReference fileReference = mStorage.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return fileReference.getDownloadUrl();
                    }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        Log.e("TAG", "then: " + downloadUri.toString());
                        if (idCategoria.equals("")){
                            idCategoria = mDatabase.push().getKey();
                        }
                        else {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference httpsReference = storage.getReferenceFromUrl(fotoUrl);
                            httpsReference.delete().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CategoriasDialogActivity.this, "Error al Guardar los Datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        fileReference.getDownloadUrl();
                        Categoria Categoria = new Categoria(nombreCategoria.getText().toString(),descripcionCategoria.getText().toString(),downloadUri.toString());
                        mDatabase.child(idCategoria).setValue(Categoria);
                        Toast.makeText(CategoriasDialogActivity.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                        mProgresDialog.setVisibility(View.GONE);
                        finish();

                    } else
                    {
                        Toast.makeText(CategoriasDialogActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CategoriasDialogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            if (idCategoria.equals(""))
                Toast.makeText(this, "Ningun archivo seleccionado", Toast.LENGTH_SHORT).show();
            else {
                if (changeFoto == false){
                    mProgresDialog.setVisibility(View.VISIBLE);
                    Categoria Categoria = new Categoria(nombreCategoria.getText().toString(),descripcionCategoria.getText().toString(),fotoUrl);
                    mDatabase.child(idCategoria).setValue(Categoria);
                    Toast.makeText(CategoriasDialogActivity.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                    mProgresDialog.setVisibility(View.GONE);
                    finish();
                }
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            changeFoto = true;
            Glide.with(this).load(mImageUri).into(imageViewFoto);
        }
    }
}