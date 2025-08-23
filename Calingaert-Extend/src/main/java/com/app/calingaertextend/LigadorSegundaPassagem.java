package com.app.calingaertextend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LigadorSegundaPassagem {

    private Map<String, Integer> tabelaGlobal;

    public LigadorSegundaPassagem(Map<String, Integer> tabelaGlobal) {
        this.tabelaGlobal = tabelaGlobal;
    }

    public void executarPassagem(List<File> modulos, String nomeArquivoSaida) {
        int baseEndereco = 0;
        int enderecoExecucaoInicial = -1;
        int tamanhoTotal = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivoSaida))) {

            System.out.println("--- Iniciando a Segunda Passagem do Ligador ---");

            for (File moduloFile : modulos) {
                LeitorObjeto leitor = new LeitorObjeto();
                LeitorObjeto.ModuloObjeto modulo = leitor.lerArquivoObjeto(moduloFile.getPath());

                if (modulo == null) {
                    System.err.println("Erro: Não foi possível ler o arquivo objeto: " + moduloFile.getName());
                    continue;
                }
                
                if (enderecoExecucaoInicial == -1 && modulo.enderecoInicial != 0) {
                    enderecoExecucaoInicial = baseEndereco + modulo.enderecoInicial;
                }
                
                for (int i = 0; i < modulo.codigoObjeto.size(); i++) {
                    String linhaObjeto = modulo.codigoObjeto.get(i);
                    String[] partes = linhaObjeto.split("\\s+");
                    
                    String novaLinha = partes[0];
                    
                    for (int j = 1; j < partes.length; j++) {
                        String operando = partes[j];
                        int novoEndereco = 0;

                        if (modulo.mapaDeRelocacao.contains(i + 1)) {
                            int enderecoRelativo = Integer.parseInt(operando);
                            novoEndereco = enderecoRelativo + baseEndereco;
                            novaLinha += " " + String.format("%02d", novoEndereco);
                        } 
                        else if (modulo.simbolosExternos.contains(operando)) {
                            if (tabelaGlobal.containsKey(operando)) {
                                novoEndereco = tabelaGlobal.get(operando);
                                novaLinha += " " + String.format("%02d", novoEndereco);
                            } else {
                                System.err.println("Erro de ligação: Símbolo externo '" + operando + "' não definido. Módulo: " + moduloFile.getName());
                                novaLinha += " ??"; 
                            }
                        } else {
                             novaLinha += " " + operando;
                        }
                    }
                    writer.write(novaLinha);
                    writer.newLine();
                }
                
                baseEndereco += modulo.tamanho;
            }
            
            tamanhoTotal = baseEndereco;

            System.out.println("--- Ligação concluída ---");
            System.out.println("Tamanho do Módulo de Carga: " + tamanhoTotal);
            System.out.println("Endereço inicial para execução: " + enderecoExecucaoInicial);
            System.out.println("Arquivo executável gerado: " + nomeArquivoSaida);

        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo de saída: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico ao processar um operando.");
        }
    }
}