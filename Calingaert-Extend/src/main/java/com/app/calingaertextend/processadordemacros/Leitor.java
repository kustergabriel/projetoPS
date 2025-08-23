package com.app.calingaertextend.processadordemacros;

import java.io.*;
import java.util.*;

public class Leitor { 
    
    private final List<ListaAsm> linhasFeitas = new ArrayList<>();
    private final Set<String> macrosDefinidas = new HashSet<>();

    public void lerArquivo(String caminho) {
        int nivelAninhamento = 0;
        boolean aguardandoCabecalho = false;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linhaOriginal;

            while ((linhaOriginal = br.readLine()) != null) {
                String linhaProcessada = linhaOriginal;

                int commentIndex = linhaProcessada.indexOf('*');
                if (commentIndex != -1) {
                    linhaProcessada = linhaProcessada.substring(0, commentIndex);
                }

                linhaProcessada = linhaProcessada.trim();

                if (linhaProcessada.isEmpty()) {
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
                    String nomeMacroAtual = tokens.length > 1 && tokens[0].startsWith("&") ? tokens[1] : tokens[0];
                    macrosDefinidas.add(nomeMacroAtual);
                    aguardandoCabecalho = false;
                } else if (nivelAninhamento > 0) {
                    tipo = "codigo_macro";
                } else if (macrosDefinidas.contains(primeiraPalavra) || (tokens.length > 1 && macrosDefinidas.contains(tokens[1]))) {
                    tipo = "chamada_macro";
                } else {
                    tipo = "codigo";
                }

                linhasFeitas.add(new ListaAsm(linhaProcessada, tipo));
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

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