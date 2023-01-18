//package com.example.projetodisp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//
//public class MeusLivrosActivity extends AppCompatActivity {
//
//    private Button btn_catalogo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_meuslivros);
//        btn_catalogo = findViewById(R.id.btn_catalogo);
//        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // back button pressed
//                finish();
//            }
//        });
//        btn_catalogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MeusLivrosActivity.this, CatalogoActivity.class));
//            }
//        });
//    }
//}




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
import android.widget.Toast;

import com.example.projetodisp.modelo.Livros;
import com.example.projetodisp.modelo.Solicitacoes;
import com.example.projetodisp.modelo.Usuarios;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MeusLivrosActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference solicitacoes = referencia.child("Livros");
    private Button btn_catalogo;
    private Button btn_adicionar;
    private Button btn_solicitacoes;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meuslivros);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        btn_solicitacoes = findViewById(R.id.btn_solicitacoes);
        btn_catalogo = findViewById(R.id.btn_catalogo);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(null);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
                startActivity(new Intent(MeusLivrosActivity.this, MainActivity.class));

            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Query query = db.getReference().child("Usuarios").child(user.getUid()).child("Meus livros");

        btn_catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeusLivrosActivity.this, CatalogoActivity.class));
            }
        });

        FirebaseRecyclerOptions<Usuarios> options = new FirebaseRecyclerOptions.Builder<Usuarios>().setQuery(query, Usuarios.class).build();
        adapter = new FirebaseRecyclerAdapter<Usuarios, ViewHolder>(options) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position, Usuarios model) {
                holder.bind(model);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_meuslivros, parent, false);

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
        Usuarios l;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nome);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MeusLivrosDetalhes.class);
                    intent.putExtra("l",l);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void bind(Usuarios usuarios) {
            l = usuarios;
            name.setText(usuarios.getLivros());

        }
    }

}