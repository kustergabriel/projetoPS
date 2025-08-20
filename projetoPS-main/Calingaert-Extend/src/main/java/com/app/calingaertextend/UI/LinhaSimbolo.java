package com.app.calingaertextend.UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LinhaSimbolo {
    private final SimpleIntegerProperty endereco;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty tipo;
    private final SimpleStringProperty status;

    public LinhaSimbolo(int endereco, String nome, String tipo, String status) {
        this.endereco = new SimpleIntegerProperty(endereco);
        this.nome = new SimpleStringProperty(nome);
        this.tipo = new SimpleStringProperty(tipo);
        this.status = new SimpleStringProperty(status);
    }

    public String getNome() {
        return nome.get();
    }

    public int getEndereco() {
        return endereco.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getTipo() {
        return tipo.get();
    }

}
