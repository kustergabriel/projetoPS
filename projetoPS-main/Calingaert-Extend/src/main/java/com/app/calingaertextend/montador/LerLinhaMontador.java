package com.app.calingaertextend.montador;


public class LerLinhaMontador {

    // Método estático para poder chamar direto pela classe sem criar objeto
    public static String processarLinha(String linha) {
        String[] palavras = linha.split(" "); // divide no espaço

        String primeiraPalavra = palavras[0]; // pega a primeira palavra
        System.out.println(primeiraPalavra); // imprime a primeira palavra
        return primeiraPalavra;
        
    }

}