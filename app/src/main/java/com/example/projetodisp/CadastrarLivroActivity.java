package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.projetodisp.modelo.Usuario;


import com.example.projetodisp.modelo.Livros;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CadastrarLivroActivity extends AppCompatActivity {

    private EditText et_nomeDoLivro,et_autor,et_paginas,et_editora, et_quantidade;

    private ValueEventListener valueEventListener;
    String[] mensagens = {"Preencha todos os campos", "Livro cadastrado com sucesso!"};


    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastrar_livro);
        Button buttonLivros = (Button)findViewById(R.id.buttonLivros);
        Button buttonSelect = (Button)findViewById(R.id.btnSelect);
        imageView = findViewById(R.id.imgView);
        et_nomeDoLivro = findViewById(R.id.et_nomeDoLivro);
        et_autor = findViewById(R.id.et_autor);
        et_paginas = findViewById(R.id.et_paginas);
        et_editora = findViewById(R.id.et_editora);
        et_quantidade = findViewById(R.id.et_quantidade);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();

            }
        });

        buttonLivros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nomeDoLivro = et_nomeDoLivro.getText().toString();
                String autor = et_autor.getText().toString();
                String paginas = et_paginas.getText().toString();
                String editora = et_editora.getText().toString();
                String quantidade = et_quantidade.getText().toString();


                if (nomeDoLivro.isEmpty() || autor.isEmpty() || paginas.isEmpty() || editora.isEmpty() || quantidade.isEmpty()){
                    Toast.makeText(CadastrarLivroActivity.this,mensagens[0], Toast.LENGTH_LONG).show();

                } else {
                    cadastrarLivro();
                    uploadImage();

                    et_nomeDoLivro.setText("");
                    et_autor.setText("");
                    et_paginas.setText("");
                    et_editora.setText("");
                    et_quantidade.setText("");
//                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_LONG);
//                    snackbar.setBackgroundTint(Color.WHITE);
//                    snackbar.setTextColor(Color.BLACK);
//                    snackbar.show();
                    Toast.makeText(CadastrarLivroActivity.this,mensagens[1], Toast.LENGTH_LONG).show();

                }

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void cadastrarLivro() {
        Livros l = new Livros();
        l.setNome(et_nomeDoLivro.getText().toString());
        l.setAutor(et_autor.getText().toString());
        l.setPaginas(Integer.parseInt(String.valueOf(et_paginas.getText())));
        l.setEditora(et_editora.getText().toString());
        l.setQuantidade(Integer.parseInt(String.valueOf(et_quantidade.getText())));
        l.dbSalvarDadosLivros();

    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null) {
            String nomeDoLivro2 = et_nomeDoLivro.getText().toString();
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/" + nomeDoLivro2
                    );

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(CadastrarLivroActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(CadastrarLivroActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });


        }
    }


}