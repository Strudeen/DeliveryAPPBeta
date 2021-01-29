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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.CriStru.orurodeliveryapp.Adapters.SubCategorias.SubCategoriasAdapter;
import com.CriStru.orurodeliveryapp.Models.Producto;
import com.CriStru.orurodeliveryapp.Models.SubCategoria;
import com.CriStru.orurodeliveryapp.Models.SubCategoriaDialog;
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

import java.util.ArrayList;
import java.util.List;

public class ProductosDialogActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 3;
    private EditText nombre,descripcion,precio,stock;
    private Spinner spinner;
    private ImageView mImageView;
    private Button guardar,cancelar,cargarimagen;
    private List<SubCategoriaDialog> dialogList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private String idCategoria="",idSubCategoria="",idProducto="",nombreSub,fotoUrl;
    private Uri mImageUri;
    private Bundle extras;
    private ProgressBar mProgressBarD;
    private boolean changeFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_dialog);
        this.setFinishOnTouchOutside(false);
        extras=getIntent().getExtras();
        mStorage = FirebaseStorage.getInstance().getReference().child("Producto");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        cargarProducto();
        setUpView();
    }

    private void cargarProducto() {
        setUpView();
        if (!extras.getString("idProductoD").equals("")){
            if (extras.getString("idCategoriaSubD").equals("")){
                idProducto=extras.getString("idProductoD");
                mDatabase.child("Producto").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            nombre.setText(dataSnapshot.child("nombre").getValue().toString());
                            descripcion.setText(dataSnapshot.child("descripcion").getValue().toString());
                            precio.setText(dataSnapshot.child("precio").getValue().toString());
                            stock.setText(dataSnapshot.child("stock").getValue().toString());
                            Glide.with(ProductosDialogActivity.this).load(dataSnapshot.child("fotoUrl").getValue().toString()).into(mImageView);
                            idSubCategoria = dataSnapshot.child("subCategoria").getValue().toString();
                            fotoUrl = dataSnapshot.child("fotoUrl").getValue().toString();
                            mDatabase.child("SubCategorias").child(idSubCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        idCategoria = dataSnapshot.child("categoria").getValue().toString();
                                        nombreSub = dataSnapshot.child("nombre").getValue().toString();
                                        dialogList.add(new SubCategoriaDialog(idSubCategoria,nombreSub));
                                        cargarSpinner();
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
        }
        if (!extras.getString("idCategoriaSubD").equals("")){
            idCategoria=extras.getString("idCategoriaSubD");
            cargarSpinner();
        }
    }

    private void setUpView() {
        nombre = findViewById(R.id.etNombreProductoD);
        descripcion = findViewById(R.id.etDescripcionProdcutoD);
        precio = findViewById(R.id.etPrecioD);
        stock = findViewById(R.id.etStockD);
        spinner = findViewById(R.id.SpinnerProductosD);
        mImageView = findViewById(R.id.imageViewFotoProductoD);
        guardar = findViewById(R.id.btnGuardarProductoD);
        cancelar = findViewById(R.id.btnCancelarProductoD);
        cargarimagen = findViewById(R.id.btnCargarImagenProductoD);
        mProgressBarD = findViewById(R.id.progress_bar_proD);

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

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                        if (idProducto.equals("")){
                            idProducto = mDatabase.child("Producto").push().getKey();
                        }
                        else {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference httpsReference = storage.getReferenceFromUrl(fotoUrl);
                            httpsReference.delete().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProductosDialogActivity.this, "Error al Guardar los Datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        fileReference.getDownloadUrl();
                        Producto producto = new Producto(idSubCategoria,nombre.getText().toString(),descripcion.getText().toString(),downloadUri.toString(),Integer.parseInt(stock.getText().toString()),Float.parseFloat(precio.getText().toString()));
                        mDatabase.child("Producto").child(idProducto).setValue(producto);
                        Toast.makeText(ProductosDialogActivity.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                        mProgressBarD.setVisibility(View.GONE);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductosDialogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (idProducto.equals(""))
                Toast.makeText(this, "Ningun archivo seleccionado", Toast.LENGTH_SHORT).show();
            else {
                if (changeFoto == false){
                    mProgressBarD.setVisibility(View.VISIBLE);
                    Producto producto = new Producto(idSubCategoria,nombre.getText().toString(),descripcion.getText().toString(),fotoUrl,Integer.parseInt(stock.getText().toString()),Float.parseFloat(precio.getText().toString()));
                    mDatabase.child("Producto").child(idProducto).setValue(producto);
                    Toast.makeText(ProductosDialogActivity.this, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                    mProgressBarD.setVisibility(View.GONE);
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

    public boolean ValidateForm() {

        boolean valid = true;
        String nombrep=nombre.getText().toString();
        if (TextUtils.isEmpty(nombrep)){
            nombre.setError("Este campo es obligatorio");
            valid=false;
        }else {
            nombre.setError(null);
        }
        String descripcionp = descripcion.getText().toString();
        if (TextUtils.isEmpty(descripcionp)){
            descripcion.setError("Este campo es obligatorio");
            valid = false;
        }else {
            descripcion.setError(null);
        }
        String preciop = precio.getText().toString();
        if (TextUtils.isEmpty(preciop)){
            precio.setError("Este campo es obligatorio");
            valid = false;
        }else {
            precio.setError(null);
        }
        String stockp = stock.getText().toString();
        if (TextUtils.isEmpty(stockp)){
            stock.setError("Este campo es obligatorio");
            valid = false;
        }else {
            stock.setError(null);
        }
        return  valid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            changeFoto = true;
            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }

    private void cargarSpinner(){
        mDatabase.child("SubCategorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds:
                            dataSnapshot.getChildren()){
                        if (ds.child("categoria").getValue().toString().equals(idCategoria)) {
                            String Nombre=ds.child("nombre").getValue().toString();
                            String id =ds.getKey();
                            dialogList.add(new SubCategoriaDialog(id,Nombre));
                        }
                    }
                    ArrayAdapter<SubCategoriaDialog> adapter = new ArrayAdapter<>(ProductosDialogActivity.this,android.R.layout.simple_dropdown_item_1line,dialogList);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            idSubCategoria = dialogList.get(position).getId().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}