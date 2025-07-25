package com.app.calingaertextend.processadordemacros;

import java.io.*;
import java.util.*;

// Classe que lê o arquivo e classifica cada linha
public class Leitor {
    private List<ListaAsm> linhasFeitas = new ArrayList<>();
    private Set<String> macrosDefinidas = new HashSet<>();

    public void lerArquivo(String caminho) {
        int nivelAninhamento = 0;
        boolean aguardandoCabecalho = false;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linhaOriginal;

            while ((linhaOriginal = br.readLine()) != null) {
                String linhaProcessada = linhaOriginal.trim();
                if (linhaProcessada.isEmpty() || linhaProcessada.startsWith("*")) {
                    continue; 
                }

                String[] tokens = linhaProcessada.toUpperCase().split("\\s+");
                String primeiraPalavra = tokens[0];
                String tipo = "";

                if (primeiraPalavra.equals("MACRO")) {
                    tipo = "inicio_macro";
                    nivelAninhamento++;
                    aguardandoCabecalho = true;

                } else if (primeiraPalavra.equals("MEND")) {
                    tipo = "fim_macro";

                    if (nivelAninhamento > 0) {
                        nivelAninhamento--;
                    }
                    aguardandoCabecalho = false;

                } else if (nivelAninhamento > 0 && aguardandoCabecalho) {
                    tipo = "cabecalho_macro";
                    
                    String nomeMacroAtual;
                    if (tokens.length > 1 && tokens[0].startsWith("&")) {
                        nomeMacroAtual = tokens[1];
                    } else {
                        nomeMacroAtual = tokens[0];
                    }
                    macrosDefinidas.add(nomeMacroAtual);
                    aguardandoCabecalho = false;

                } else if (nivelAninhamento > 0) {
                    tipo = "codigo_macro";

                } else if (macrosDefinidas.contains(primeiraPalavra)) {
                    tipo = "chamada_macro";

                } else if (tokens.length > 1 && macrosDefinidas.contains(tokens[1])) {
                    tipo = "chamada_macro";

                } else {
                    tipo = "codigo";
                }

                linhasFeitas.add(new ListaAsm(linhaOriginal, tipo));
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Exibe as linhas classificadas para verificação
        System.out.println("\n<<<< Linhas classificadas pelo Leitor >>>>");
        for (ListaAsm linha : linhasFeitas) {
            System.out.println(String.format("%-18s -> %s", linha.getTipo(), linha.getConteudo()));
        }
        System.out.println("--- Fim da Fase 1 ---");
    }

    public List<ListaAsm> getLinhasFeitas() {
        return linhasFeitas;
    }
}