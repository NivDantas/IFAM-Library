package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetodisp.modelo.Livros;
import com.example.projetodisp.modelo.Solicitacoes;
//import com.example.projetodisp.modelo.Usuario;
import com.example.projetodisp.modelo.Usuarios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetalhesLivros extends AppCompatActivity {

    private Button btn_solicitarReserva;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_livros);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        btn_solicitarReserva = findViewById(R.id.btn_solicitarReserva);

        imageView = findViewById(R.id.imgView);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });
        Livros l = (Livros) getIntent().getSerializableExtra("l");

        mAuth = FirebaseAuth.getInstance();


        TextView nome = findViewById(R.id.nome);
        TextView data = findViewById(R.id.autor);
        TextView horario = findViewById(R.id.paginas);
        TextView editora = findViewById(R.id.editora);

        nome.setText(l.getNome());
        data.setText("Autor: " + l.getAutor());
        horario.setText("Páginas: " + l.getPaginas());
        editora.setText("Editora: " + l.getEditora());

        String imageID = l.getNome();

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

                            Toast.makeText(DetalhesLivros.this, "Failed to retrieve", Toast.LENGTH_SHORT);
                        }
                    });
        } catch (IOException e){
            e.printStackTrace();
        }



        btn_solicitarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarReserva();

            }
        });
    }


    public void solicitarReserva() {
        Livros l = (Livros) getIntent().getSerializableExtra("l");
        Usuarios u = (Usuarios) getIntent().getSerializableExtra("u");
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference usuarios = referencia.child("Usuarios").child(user.getUid()).child("Informações Pessoais");

        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue(String.class);
                Solicitacoes s = new Solicitacoes();
                s.setNome(nome);
                s.setId(user.getUid());
                s.setNomeDoLivro(l.getNome());
                s.setAutor(l.getAutor());
                s.setEditora(l.getEditora());
                s.setPaginas(l.getPaginas());
                s.salvarDadosSolicitacoes();
                Toast.makeText(DetalhesLivros.this, "Solicitação Realizada!.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




    }
}