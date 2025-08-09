package com.app.calingaertextend.montador;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    private Map<String, Simbolos> simbolos ;

    public TabelaDeSimbolos() {
        simbolos = new LinkedHashMap<>();
    }

    public void adicionarSimbolo(String nome, int endereco, String tipo, String status, boolean isGlobal) {
        if (!simboloValido(nome)) {
            throw new RuntimeException("Símbolo inválido: " + nome + " (Máx 8 caracteres, inicia com letra, alfanumérico)");
        }

        if (!simbolos.containsKey(nome)) {
            simbolos.put(nome, new Simbolos(nome, endereco, tipo, status, isGlobal));
        }
    }

    public Simbolos buscarSimbolo(String nome) {
        return simbolos.get(nome);
    }


    public Collection<Simbolos> getTodosSimbolos() {
        return simbolos.values();
    }

   public static boolean simboloValido(String nome) {
    // Permite símbolo iniciar com &, depois aplica regra original
    if (nome.startsWith("&")) {
        nome = nome.substring(1); // remove o &
    }
    return nome.matches("^[A-Za-z][A-Za-z0-9]{0,7}$");
}

    public void imprimirTabela() {
        System.out.println("NOME\tENDERECO\tTIPO\tSTATUS");
        for (Simbolos s : simbolos.values()) {
            System.out.println(s);
        }
    }
    public boolean contemSimbolo(String nome) {
    // Supondo que você tenha um Map chamado simbolos para armazenar os símbolos
    return simbolos.containsKey(nome);
    }
}
