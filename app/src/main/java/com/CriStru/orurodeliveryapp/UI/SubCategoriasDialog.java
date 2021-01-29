package com.CriStru.orurodeliveryapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Models.Categoria;
import com.CriStru.orurodeliveryapp.Models.SubCategoria;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SubCategoriasDialog extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;
    private EditText nombreSubCategoria,descripcionSubcategoria;
    private Button guardar,cancelar,cargarimagen;
    private ImageView imageView;
    private ProgressBar mProgressBarD;
    private String idCategoria="",idSubCategoria="",fotoUrl;
    private Bundle extras;
    private Uri mImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private boolean changeFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorias_dialog);
        this.setFinishOnTouchOutside(false);
        configureServices();
        setupView();
    }

    private void configureServices() {
        extras=getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SubCategorias");
        if (!extras.getString("idCategoriaSubD").equals("")){
            idCategoria=extras.getString("idCategoriaSubD");
        }
        if (!extras.getString("idSubCategoriaSubD").equals("")){
            idSubCategoria=extras.getString("idSubCategoriaSubD");
            Log.d("idSubCategoriaSubD",idSubCategoria);
            mDatabase.child(idSubCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        nombreSubCategoria.setText(dataSnapshot.child("nombre").getValue().toString());
                        descripcionSubcategoria.setText(dataSnapshot.child("descripcion").getValue().toString());
                        Glide.with(SubCategoriasDialog.this).load(dataSnapshot.child("fotoUrl").getValue().toString()).into(imageView);
                        fotoUrl = dataSnapshot.child("fotoUrl").getValue().toString();
                        idCategoria = dataSnapshot.child("categoria").getValue().toString();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void setupView() {
        nombreSubCategoria = findViewById(R.id.etNombreSubCategoriasD);
        descripcionSubcategoria = findViewById(R.id.etDescripcionSubCategoriasD);
        guardar = findViewById(R.id.btnGuardarSubCategoriasD);
        cancelar = findViewById(R.id.btnCancelarSubCategoriasD);
        cargarimagen = findViewById(R.id.btnCargarImagenSubCategoriasD);
        imageView = findViewById(R.id.imageViewFotoSubCategoriaD);
        mProgressBarD = findViewById(R.id.progress_bar_subD);
        mProgressBarD.setVisibility(View.GONE);
        mStorage = FirebaseStorage.getInstance().getReference().child("SubCategorias");
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        cargarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openFileChooser();
            }
        });
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
            Glide.with(this).load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public boolean ValidateForm() {

        boolean valid = true;
        String nombresub=nombreSubCategoria.getText().toString();
        if (TextUtils.isEmpty(nombresub)){
            nombreSubCategoria.setError("Este campo es obligatorio");
            valid=false;
        }else {
            nombreSubCategoria.setError(null);
        }
        String descripcionsub = descripcionSubcategoria.getText().toString();
        if (TextUtils.isEmpty(descripcionsub)){
            descripcionSubcategoria.setError("Este campo es obligatorio");
            valid = false;
        }else {
            descripcionSubcategoria.setError(null);
        }
        return  valid;
    }

    private void uploadFile() {
        if (!ValidateForm())
            return;
        mProgressBarD.setVisibility(View.VISIBLE);
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
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Log.e("TAG", "then: " + downloadUri.toString());
                        if (idSubCategoria.equals("")){
                            idSubCategoria = mDatabase.push().getKey();
                        }
                        else {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference httpsReference = storage.getReferenceFromUrl(fotoUrl);
                        httpsReference.delete().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SubCategoriasDialog.this, "Error al Guardar los Datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                        }
                        fileReference.getDownloadUrl();
                        SubCategoria subCategoria = new SubCategoria(idCategoria,nombreSubCategoria.getText().toString(),descripcionSubcategoria.getText().toString(),downloadUri.toString());
                        mDatabase.child(idSubCategoria).setValue(subCategoria);
                        Toast.makeText(SubCategoriasDialog.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                        mProgressBarD.setVisibility(View.GONE);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SubCategoriasDialog.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SubCategoriasDialog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (idSubCategoria.equals(""))
                Toast.makeText(this, "Ningun archivo seleccionado", Toast.LENGTH_SHORT).show();
            else {
                if (changeFoto == false){
                    mProgressBarD.setVisibility(View.VISIBLE);
                    SubCategoria subCategoria = new SubCategoria(idCategoria,nombreSubCategoria.getText().toString(),descripcionSubcategoria.getText().toString(),fotoUrl);
                    mDatabase.child(idSubCategoria).setValue(subCategoria);
                    Toast.makeText(SubCategoriasDialog.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                    mProgressBarD.setVisibility(View.GONE);
                    finish();
                }
            }
        }
    }
}