package com.app.calingaertextend.montador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VerificarLabel {

    // Lista simplificada de instruções conhecidas
    private static final String[] INSTRUCOES = {
        "MACRO", "MEND", "LOAD", "STORE", "MULT", "DIV",
        "SHIFTR", "SHIFTL", "SUB", "READ", "WRITE", "STOP",
        "END", "SPACE", "SCALE", "DISCR", "MULTSC", "DIVSC"
    };

    public static boolean linhaTemLabel(String linha) {
        linha = linha.trim();
        if (linha.isEmpty() || linha.startsWith("*")) {
            return false;
        }

        String[] partes = linha.split("\\s+");
        if (partes.length == 0) return false;

        String primeiraPalavra = partes[0];

        for (String instrucao : INSTRUCOES) {
            if (primeiraPalavra.equals(instrucao)) {
                return false; // Começa com uma instrução, então não é label
            }
        }

        return true; // Primeira palavra não é uma instrução, consideramos label
    }

    public static void verificarLabelsNoArquivo(String caminhoArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int numeroLinha = 1;
            while ((linha = br.readLine()) != null) {
                if (linhaTemLabel(linha)) {
                    System.out.println("Label encontrada na linha " + numeroLinha + ": " + linha);
                }
                numeroLinha++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}    