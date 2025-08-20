package com.app.calingaertextend.montador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//package com.app.calingaertextend;

import java.io.*;
import java.util.*;

public class SegundaPassagem {

    public void segundapassagem(String caminhoArquivoEntrada, String caminhoArquivoSaida,
                                TabelaDeSimbolos tabelaSimbolos,
                                TabelaInstrucao tabelaInstrucao) {

        int linhaAtual = 0;
        boolean emDefinicaoDeMacro = false;

        // Estruturas para o formato do LeitorObjeto
        List<String> codigoObjeto = new ArrayList<>();
        Map<String, Integer> simbolosGlobais = new HashMap<>();
        List<String> simbolosExternos = new ArrayList<>();
        List<Integer> mapaDeRelocacao = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivoEntrada));
             BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivoSaida))) {

            String linha;

            System.out.println("\n--- A gerar código objeto para o ficheiro: " + caminhoArquivoSaida + " ---");

            while ((linha = br.readLine()) != null) {
                linhaAtual++;

                // Remove o comentário em linha ANTES de qualquer outra coisa
                int commentIndex = linha.indexOf('*');
                if (commentIndex != -1) {
                    linha = linha.substring(0, commentIndex);
                }

                linha = linha.trim();
                    
                if (linha.isEmpty()) {
                    continue;
                }
                
                if (linha.toUpperCase().startsWith("MACRO")) {
                    emDefinicaoDeMacro = true;
                    continue;
                }
                if (linha.toUpperCase().startsWith("MEND")) {
                    emDefinicaoDeMacro = false;
                    continue;
                }
                if (emDefinicaoDeMacro) {
                    continue;
                }

                String[] partes = linha.split("\\s+");
                String rotulo = null;
                String instrucao = null;
                List<String> operandos = new ArrayList<>();

                int indiceInstrucao = -1;
                if (tabelaInstrucao.contains(partes[0].toUpperCase())) {
                    indiceInstrucao = 0;
                } else if (partes.length > 1 && tabelaInstrucao.contains(partes[1].toUpperCase())) {
                    rotulo = partes[0];
                    indiceInstrucao = 1;
                }

                if (indiceInstrucao != -1) {
                    instrucao = partes[indiceInstrucao].toUpperCase();
                    for (int i = indiceInstrucao + 1; i < partes.length; i++) {
                        operandos.add(partes[i]);
                    }
                } else {
                    continue;
                }

                if (instrucao.equals("START") || instrucao.equals("END")) {
                    continue;
                }

                if (instrucao.equals("SPACE")) {
                    codigoObjeto.add("00");
                    mapaDeRelocacao.add(0);
                } else if (instrucao.equals("CONST")) {
                    if (!operandos.isEmpty()) {
                        codigoObjeto.add(String.format("%02d", Integer.parseInt(operandos.get(0))));
                        mapaDeRelocacao.add(0);
                    }
                } else {
                    int opcode = tabelaInstrucao.getOpcode(instrucao);
                    if (opcode == -1) {
                        continue;
                    }

                    StringBuilder linhaCodigo = new StringBuilder();
                    linhaCodigo.append(String.format("%02d", opcode));

                    for (String operando : operandos) {
                        Simbolos simbolo = tabelaSimbolos.buscarSimbolo(operando);
                        if (simbolo != null) {
                            linhaCodigo.append(" ").append(String.format("%02d", simbolo.getEndereco()));
                            mapaDeRelocacao.add(1);
                            // se o símbolo for global, coloca nos globais
                            if (simbolo.isGlobal()) {
                                simbolosGlobais.put(simbolo.getRotulo(), simbolo.getEndereco());
                            }
                        } else {
                            simbolosExternos.add(operando);
                            linhaCodigo.append(" ??");
                            mapaDeRelocacao.add(1);
                        }
                    }
                    codigoObjeto.add(linhaCodigo.toString());
                }
            }

            // Agora escreve no formato do LeitorObjeto
            writer.write("TAMANHO: " + codigoObjeto.size());
            writer.newLine();
            writer.write("ENDERECO_INICIAL: 0"); // se for absoluto, alterar aqui
            writer.newLine();

            writer.write("--- GLOBAIS ---");
            writer.newLine();
            for (Map.Entry<String, Integer> entrada : simbolosGlobais.entrySet()) {
                writer.write(entrada.getKey() + " " + entrada.getValue());
                writer.newLine();
            }

            writer.write("--- EXTERNOS ---");
            writer.newLine();
            for (String ext : simbolosExternos) {
                writer.write(ext);
                writer.newLine();
            }

            writer.write("--- RELOCACAO ---");
            writer.newLine();
            writer.write(mapaDeRelocacao.toString().replaceAll("[\\[\\] ]", ""));
            writer.newLine();

            writer.write("--- CODIGO ---");
            writer.newLine();
            for (String linhaCod : codigoObjeto) {
                writer.write(linhaCod);
                writer.newLine();
            }

            System.out.println("--- Geração de código concluída com sucesso! ---");

        } catch (IOException e) {
            System.err.println("Erro ao ler ou escrever no ficheiro: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado na linha " + linhaAtual + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
