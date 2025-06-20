package com.app.calingaertextend.UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LinhaMemoria {
    private final SimpleStringProperty endereco;
    private final SimpleIntegerProperty valor;

    public LinhaMemoria(String endereco, int valor) {
        this.endereco = new SimpleStringProperty(endereco);
        this.valor = new SimpleIntegerProperty(valor);
    }

    public String getEndereco(){
        return endereco.get();
    }

    public int getValor(){
        return valor.get();
    }

    public void setValor(int novoValor){
        valor.set(novoValor);
    }
}
