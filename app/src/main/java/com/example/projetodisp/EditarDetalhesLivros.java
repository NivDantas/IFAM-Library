package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetodisp.modelo.Livros;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarDetalhesLivros extends AppCompatActivity {

    private TextView nome,autor,paginas,editora,quantidade;
    private Button btn_mais;
    private Button btn_menos;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_detalhes_livros);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        nome = findViewById(R.id.nome);
        autor = findViewById(R.id.autor);
        paginas = findViewById(R.id.paginas);
        editora = findViewById(R.id.editora);
        btn_mais = findViewById(R.id.btn_mais);
        btn_menos = findViewById(R.id.btn_menos);
        quantidade = findViewById(R.id.quantidade);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        Livros l = (Livros) getIntent().getSerializableExtra("l");

        nome.setText(l.getNome());
        autor.setText("Autor: " + l.getAutor());
        paginas.setText("Páginas: " + l.getPaginas());
        editora.setText("Editora: " + l.getEditora());
        quantidade.setText("" + l.getQuantidade());


        btn_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menosLivros();
            }
        });

        btn_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maisLivros();
            }
        });


    }

    public void maisLivros() {
        Livros l = (Livros) getIntent().getSerializableExtra("l");

        DatabaseReference usuarios = referencia.child("Livros").child(l.getNome());

        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int quantidade =  dataSnapshot.child("quantidade").getValue(int.class);
                l.setQuantidade(quantidade+1);
                l.dbSalvarDadosLivros();
                startActivity(new Intent(EditarDetalhesLivros.this, CatalogoBibliotecario.class));
                Toast.makeText(EditarDetalhesLivros.this,"1 unidade adicionada no catálogo!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void menosLivros() {
        Livros l = (Livros) getIntent().getSerializableExtra("l");

        DatabaseReference usuarios = referencia.child("Livros").child(l.getNome());

        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int quantidade =  dataSnapshot.child("quantidade").getValue(int.class);
                if (quantidade > 0) {
                    l.setQuantidade(quantidade - 1);
                    l.dbSalvarDadosLivros();
                    startActivity(new Intent(EditarDetalhesLivros.this, CatalogoBibliotecario.class));
                    Toast.makeText(EditarDetalhesLivros.this, "1 unidade removida do catálogo!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarDetalhesLivros.this, "Este livro já está sem unidade disponíveis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}