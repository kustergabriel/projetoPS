package com.app.calingaertextend;

import java.util.List;

public class Macro {
    private String nome;
    private List<String> parametros;
    private List<String> corpo;

    public Macro(String nome, List<String> parametros, List<String> corpo) {
        this.nome = nome;
        this.parametros = parametros;
        this.corpo = corpo;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getParametros() {
        return parametros;
    }

    public List<String> getCorpo() {
        return corpo;
    }
}
