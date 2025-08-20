package com.app.calingaertextend.UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LinhaRegistrador {
    private final SimpleStringProperty nome;
    private final SimpleIntegerProperty valor;

    public LinhaRegistrador(String nome, int valor) {
        this.nome = new SimpleStringProperty(nome);
        this.valor = new SimpleIntegerProperty(valor);
    }

    public String getNome(){
        return nome.get();
    }

    public int getValor(){
        return valor.get();
    }

    public void setValor(int novoValor){
        valor.set(novoValor);
    }
}
