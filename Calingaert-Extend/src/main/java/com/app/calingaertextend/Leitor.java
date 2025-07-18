package com.app.calingaertextend;

import java.io.*;
import java.util.*;

// Classe que lê o arquivo e classifica cada linha
public class Leitor {
    private List<ListaAsm> linhasFeitas = new ArrayList<>();
    private Set<String> macrosDefinidas = new HashSet<>();

    public void lerArquivo(String caminho) {
        System.out.println("\nProcessando: " + caminho);

        boolean dentroMacro = false;
        String nomeMacroAtual = "";
        boolean aguardandoCabecalho = false;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linhaOriginal;

            while ((linhaOriginal = br.readLine()) != null) {
                linhaOriginal = linhaOriginal.trim();
                if (linhaOriginal.isEmpty() || linhaOriginal.startsWith("*")) {
                    continue; // Ignora comentários e linhas vazias
                }

                // Tokenização
                String[] tokens = linhaOriginal.split("\\s+");
                List<String> tokensLimpos = new ArrayList<>();

                for (String token : tokens) {
                    if (!token.isEmpty() && !token.startsWith("*")) {
                        tokensLimpos.add(token.toUpperCase());
                    }
                }

                if (tokensLimpos.isEmpty()) continue;

                String tipo = "";
                String primeiraPalavra = tokensLimpos.get(0);

                if (primeiraPalavra.equalsIgnoreCase("MACRO")) {
                    tipo = "inicio_macro";
                    dentroMacro = true;
                    aguardandoCabecalho = true;

                } else if (primeiraPalavra.equalsIgnoreCase("MEND")) {
                    tipo = "fim_macro";
                    dentroMacro = false;
                    nomeMacroAtual = "";

                } else if (dentroMacro && aguardandoCabecalho) {
                    tipo = "cabecalho_macro";
                    nomeMacroAtual = primeiraPalavra;
                    macrosDefinidas.add(nomeMacroAtual);
                    aguardandoCabecalho = false;

                } else if (dentroMacro) {
                    tipo = "codigo_macro";

                } else if (macrosDefinidas.contains(primeiraPalavra)) {
                    tipo = "chamada_macro";

                } else if (primeiraPalavra.endsWith(":")) {
                    tipo = "label";

                } else {
                    tipo = "codigo";
                }

                linhasFeitas.add(new ListaAsm(linhaOriginal, tipo));
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Exibe as linhas classificadas
        System.out.println("\n<<<< Linhas classificadas >>>>");
        for (ListaAsm linha : linhasFeitas) {
            System.out.println(linha.getTipo() + " -> " + linha.getConteudo());
        }
        System.out.println("Total de linhas: " + linhasFeitas.size());
    }


    public List<ListaAsm> getLinhasFeitas() {
    return linhasFeitas;
    }



}
