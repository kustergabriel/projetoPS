package com.app.calingaertextend.montador;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ler_arquivo_montador {
    public static String[] lerArquivo(String caminhoArquivo) {
        ArrayList<String> linhas = new ArrayList<>();
     
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Converte ArrayList em vetor (String[])
        return linhas.toArray(new String[0]);
    }
}
