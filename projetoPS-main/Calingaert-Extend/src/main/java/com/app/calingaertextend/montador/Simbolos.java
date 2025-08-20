package com.app.calingaertextend.montador;


public class Simbolos {
    private String rotulo;
    private int endereco;
    private String tipo;
    private String status;
    private boolean isGlobal;
    

    public Simbolos(String rotulo, int endereco, String tipo, String status, boolean isGlobal) {
        this.rotulo = rotulo; // = nome do símbolo
        this.endereco = endereco;
        this.tipo = tipo;
        this.status = status;
        this.isGlobal = isGlobal; // indica se o símbolo é global ou não
    }

    public boolean isGlobal() {
        return isGlobal;
    }
    
    public String getRotulo() {
        return this.rotulo;
    }

    public int getEndereco() {
        return this.endereco;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
   
    @Override
    public String toString() {
        
        return String.format("%-8s\t%-8d\t%-8s\t%s", this.rotulo, this.endereco, this.tipo, this.status);
    }
}