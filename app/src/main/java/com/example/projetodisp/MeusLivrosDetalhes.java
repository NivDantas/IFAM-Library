package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetodisp.modelo.Livros;
import com.example.projetodisp.modelo.Usuarios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MeusLivrosDetalhes extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_livros_detalhes);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });


        Usuarios l = (Usuarios) getIntent().getSerializableExtra("l");
        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imgView);
        TextView nome = findViewById(R.id.nome);
        TextView data = findViewById(R.id.autor);

        
//
        nome.setText(l.getLivros());
        data.setText("Autor: " + l.getAutorDoLivro());
        String imageID = l.getLivros();

        storageReference = FirebaseStorage.getInstance().getReference("images/"+imageID);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MeusLivrosDetalhes.this, "Failed to retrieve", Toast.LENGTH_SHORT);
                        }
                    });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}