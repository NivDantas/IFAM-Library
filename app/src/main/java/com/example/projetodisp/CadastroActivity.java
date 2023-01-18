package com.example.projetodisp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetodisp.modelo.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText et_nomeCadastro;
    private EditText et_usuarioCadastro;
    private EditText et_senhaCadastro;
    private EditText et_emailCadastro;
    private CheckBox cb_bibliotecarioCadastro;
    private Button btn_cadastrar;
    private FirebaseAuth mAuth;
    private Usuarios u;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastro);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed

                    finish();
                startActivity(new Intent(CadastroActivity.this, MainActivity.class));


            }
        });

        et_nomeCadastro = findViewById(R.id.et_nomeCadastro);
        et_usuarioCadastro = findViewById(R.id.et_usuarioCadastro);
        et_senhaCadastro = findViewById(R.id.et_senhaCadastro);
        et_emailCadastro = findViewById(R.id.et_emailCadastro);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        cb_bibliotecarioCadastro = findViewById(R.id.cb_bibliotecarioCadastro);
        mAuth = FirebaseAuth.getInstance();

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = et_nomeCadastro.getText().toString();
                String usuario = et_usuarioCadastro.getText().toString();
                String email = et_emailCadastro.getText().toString();
                String senha = et_senhaCadastro.getText().toString();


                if (nome.isEmpty() || usuario.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    criarLogin(v);
                }
            }
        });
    }

    private void criarLogin(View v) {
        u = new Usuarios();
        u.setNome(et_nomeCadastro.getText().toString());
        u.setUsuario(et_usuarioCadastro.getText().toString());
        u.setSenha(et_senhaCadastro.getText().toString());
        u.setEmail(et_emailCadastro.getText().toString());
        if (cb_bibliotecarioCadastro.isChecked()) {
            u.setBibliotecario(true);
        } else {
            u.setBibliotecario(false);
        }

        mAuth.createUserWithEmailAndPassword(u.getEmail(), u.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            u.setId(user.getUid());
                            u.salvarDados();
                            startActivity(new Intent(CadastroActivity.this, MainActivity.class));

                        } else {
//                            Toast.makeText(CadastroActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            String erro;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha com no mínimo 6 caracteres";
                            }catch (FirebaseAuthUserCollisionException e){
                                erro = "Essa conta já foi cadastrada";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = "Email inválido";
                            }catch (Exception e) {
                                erro = "Erro ao cadastrar usuário";
                            }
                            Toast.makeText(CadastroActivity.this,erro, Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }
}