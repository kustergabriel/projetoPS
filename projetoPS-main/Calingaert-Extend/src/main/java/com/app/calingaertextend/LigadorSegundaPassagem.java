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

        // Use try-with-resources para garantir que o escritor seja fechado automaticamente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivoSaida))) {

            System.out.println("--- Iniciando a Segunda Passagem do Ligador ---");

            // Itera sobre cada módulo objeto
            for (File moduloFile : modulos) {
                LeitorObjeto leitor = new LeitorObjeto();
                LeitorObjeto.ModuloObjeto modulo = leitor.lerArquivoObjeto(moduloFile.getPath());

                if (modulo == null) {
                    System.err.println("Erro: Não foi possível ler o arquivo objeto: " + moduloFile.getName());
                    continue;
                }
                
                // Define o endereço de execução inicial do programa
                if (enderecoExecucaoInicial == -1 && modulo.enderecoInicial != 0) {
                    enderecoExecucaoInicial = baseEndereco + modulo.enderecoInicial;
                }
                
                // Processa cada linha do código objeto
                for (int i = 0; i < modulo.codigoObjeto.size(); i++) {
                    String linhaObjeto = modulo.codigoObjeto.get(i);
                    String[] partes = linhaObjeto.split("\\s+");
                    
                    // Adiciona o opcode (a primeira parte)
                    String novaLinha = partes[0];
                    
                    // Processa os operandos (as partes restantes)
                    for (int j = 1; j < partes.length; j++) {
                        String operando = partes[j];
                        int novoEndereco = 0;

                        // Verifica se o operando precisa ser relocadado (se a posição j-1 está no mapa)
                        // A posição do operando no código é 'baseEndereco + i + 1'
                        // Precisa verificar se a posição 'i + 1' está no mapa de relocação
                        if (modulo.mapaDeRelocacao.contains(i + 1)) {
                            // Se sim, é um endereço relativo que precisa de ajuste
                            int enderecoRelativo = Integer.parseInt(operando);
                            novoEndereco = enderecoRelativo + baseEndereco;
                            novaLinha += " " + String.format("%02d", novoEndereco);
                        } 
                        // Verifica se o operando é um símbolo externo
                        else if (modulo.simbolosExternos.contains(operando)) {
                            // Busca o endereço do símbolo na tabela global
                            if (tabelaGlobal.containsKey(operando)) {
                                novoEndereco = tabelaGlobal.get(operando);
                                novaLinha += " " + String.format("%02d", novoEndereco);
                            } else {
                                // ERRO: Símbolo global não definido
                                System.err.println("Erro de ligação: Símbolo externo '" + operando + "' não definido. Módulo: " + moduloFile.getName());
                                novaLinha += " ??"; // Marca o erro no código de saída
                            }
                        } else {
                             // Se não for relocável nem externo, é um valor literal.
                             novaLinha += " " + operando;
                        }
                    }
                    // Escreve a linha processada no arquivo de saída
                    writer.write(novaLinha);
                    writer.newLine();
                }
                
                // Atualiza o endereço base para o próximo módulo
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