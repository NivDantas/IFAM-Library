package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

//import com.example.projetodisp.modelo.Usuario;
import com.example.projetodisp.modelo.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private EditText et_email;
    private EditText et_senha;
    private CheckBox cb_bibliotecario;
    private Button btn_login;
    private TextView tv_cadastrar;
    private FirebaseAuth mAuth;
    private Usuarios u;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    String[] mensagens = {"Preencha todos os campos", "Login realizado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_senha = findViewById(R.id.et_senha);
        btn_login = findViewById(R.id.btn_login);
        tv_cadastrar = findViewById(R.id.tv_cadastrar);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receberDados();
                if (u.getEmail().isEmpty() || u.getSenha().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();


                } else {
                    logar();
                }

            }
        });

        tv_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cadastrar();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser usuarioAtual = mAuth.getCurrentUser();
//        if (usuarioAtual != null) {
//            startActivity(new Intent(MainActivity.this, MenuBibliotecarioActivity.class));
//        }
//    }

    private void Cadastrar() {
        startActivity(new Intent(this, CadastroActivity.class));
    }


    private void logar() {
        mAuth.signInWithEmailAndPassword(u.getEmail(), u.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            verfificarBibliotecario();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Autenticação falhou.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void receberDados() {
        u = new Usuarios();
        u.setEmail(et_email.getText().toString());
        u.setSenha(et_senha.getText().toString());
    }
    private void verfificarBibliotecario() {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference usuarios = referencia.child("Usuarios").child(user.getUid()).child("Informações Pessoais");

        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean nome = dataSnapshot.child("bibliotecario").getValue(boolean.class);
                if (nome) {
//                    Toast.makeText(MainActivity.this,"verdadeiro", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, CatalogoBibliotecario.class));
                } else {
                    startActivity(new Intent(MainActivity.this, CatalogoActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}