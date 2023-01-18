package com.example.projetodisp.modelo;

import com.example.projetodisp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.example.projetodisp.modelo.Livros;

import java.io.Serializable;
//import com.example.projetodisp.modelo.Usuario;



public class Solicitacoes implements Serializable {
    private String autor;
    private int paginas;
    private String editora;
    public String nomeDoLivro;
    public String nome;
    public String id;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeDoLivro() {
        return nomeDoLivro;
    }

    public void setNomeDoLivro(String nomeDoLivro) {
        this.nomeDoLivro = nomeDoLivro;
    }

    public void salvarDadosSolicitacoes() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("Solicitacoes").child(this.id).setValue(this);
    }
}
