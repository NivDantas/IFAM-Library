package com.example.projetodisp.modelo;


import com.example.projetodisp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Livros implements Serializable {
    private String nome;
    private String autor;
    private int paginas;
    private String editora;
    private int quantidade;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void dbSalvarDadosLivros() {
        DatabaseReference firebaseLivros = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseLivros.child("Livros").child(getNome()).setValue(this);
    }
}
