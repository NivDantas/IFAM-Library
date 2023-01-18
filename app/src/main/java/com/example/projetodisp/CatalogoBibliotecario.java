package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.projetodisp.modelo.Livros;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CatalogoBibliotecario extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference solicitacoes = referencia.child("Livros");
    private Button btn_solicitacoes;
    private Button btn_adicionar;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_bibliotecario);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        btn_solicitacoes = findViewById(R.id.btn_solicitacoes);
        btn_adicionar = findViewById(R.id.btn_adicionar);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(null);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
                startActivity(new Intent(CatalogoBibliotecario.this, MainActivity.class));

            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Query query = db.getReference().child("Livros");

        btn_solicitacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogoBibliotecario.this, SolicitacoesActivity.class));
            }
        });

        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogoBibliotecario.this, CadastrarLivroActivity.class));
            }
        });
        FirebaseRecyclerOptions<Livros> options = new FirebaseRecyclerOptions.Builder<Livros>().setQuery(query, Livros.class).build();
        adapter = new FirebaseRecyclerAdapter<Livros, ViewHolder>(options) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position, Livros model) {
                holder.bind(model);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_livros_catalogo, parent, false);

                return new ViewHolder(view);
            }


        };

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Livros l;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nome);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), EditarDetalhesLivros.class);
                    intent.putExtra("l",l);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void bind(Livros livros) {
            l = livros;
            name.setText(livros.getNome());
        }
    }



//    private void verfificarBibliotecario() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        DatabaseReference usuarios = referencia.child("Usuarios").child(user.getUid());
//
//        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean nome = dataSnapshot.child("bibliotecario").getValue(boolean.class);
//                if (nome) {
//                    startActivity(new Intent(CatalogoBibliotecario.this, DetalhesLivros.class));
//                } else {
//                    startActivity(new Intent(CatalogoBibliotecario.this, DetalhesLivros.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}