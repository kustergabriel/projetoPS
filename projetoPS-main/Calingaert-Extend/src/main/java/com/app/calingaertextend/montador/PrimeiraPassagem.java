package com.app.calingaertextend.montador;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PrimeiraPassagem {

    private boolean emEscopoLocal = false; // Variável para controlar o escopo local

    public void primeirapassagem(String caminhoArquivo, TabelaDeSimbolos tabelaSimbolos, TabelaInstrucao tabelaInstrucao) {

        int locctr = 0;
        int linhaAtual = 0;
        boolean emDefinicaoDeMacro = false;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            System.out.println("\n--- A iniciar Primeira Passagem (com cálculo de tamanho) ---");

            while ((linha = br.readLine()) != null) {

                // Ignora linhas vazias ou de comentário.
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

                // DEBUG: Imprime a linha que está a ser processada.
                System.out.println("\n[Linha " + linhaAtual + "]: '" + linha + "' | locctr atual = " + locctr);

                // Lógica para pular o conteúdo das definições de macro.
                if (linha.toUpperCase().startsWith("MACRO")) {
                    emDefinicaoDeMacro = true;
                    System.out.println("  -> Detectado início de MACRO. A ignorar conteúdo.");
                    continue;
                }
                if (linha.toUpperCase().startsWith("MEND")) {
                    emDefinicaoDeMacro = false;
                    System.out.println("  -> Detectado fim de MEND.");
                    continue;
                }
                if (emDefinicaoDeMacro) {
                    continue;
                }

                // Divide a linha em partes (tokens) para análise.
                String[] partes = linha.split("\\s+");
                String rotulo = null;
                String instrucaoOuDiretiva = null;

                // Lógica para identificar se a primeira palavra é um rótulo.
                if (!tabelaInstrucao.contains(partes[0].toUpperCase())) {
                    rotulo = partes[0];
                    if (partes.length > 1) {
                        instrucaoOuDiretiva = partes[1].toUpperCase();
                    }
                } else {
                    instrucaoOuDiretiva = partes[0].toUpperCase();
                }

                // Verifica se estamos iniciando ou terminando um escopo local
                if (instrucaoOuDiretiva != null) {
                    if (instrucaoOuDiretiva.equals("PROC")) {
                        emEscopoLocal = true; // Entrando em um escopo local
                    } else if (instrucaoOuDiretiva.equals("ENDPROC")) {
                        emEscopoLocal = false; // Saindo do escopo local
                    }
                }

                // Bloco de segurança para corrigir má identificação de START/END.
                if (rotulo != null && (rotulo.equalsIgnoreCase("START") || rotulo.equalsIgnoreCase("END"))) {
                    instrucaoOuDiretiva = rotulo.toUpperCase();
                    rotulo = null;
                }

                // Trata as diretivas de controle START e END.
                if (instrucaoOuDiretiva != null) {
                    if (instrucaoOuDiretiva.equals("START")) {
                        System.out.println("  -> Processando diretiva START.");
                        if (partes.length > 1) {
                            try {
                                locctr = Integer.parseInt(rotulo == null ? partes[1] : partes[2]);
                            } catch (NumberFormatException nfe) {
                                locctr = 0;
                            }
                        }
                        if (rotulo != null) {
                            tabelaSimbolos.adicionarSimbolo(rotulo, locctr, "PROGRAMA", "DEFINIDO", !emEscopoLocal);
                        }
                        continue;
                    } else if (instrucaoOuDiretiva.equals("END")) {
                        System.out.println("  -> Processando diretiva END. Fim da primeira passagem.");
                        break; 
                    }
                }

                // Adiciona o rótulo (se existir) na Tabela de Símbolos com o endereço ATUAL.
                if (rotulo != null) {
                    boolean isGlobal = !emEscopoLocal; // Se não estamos em um escopo local, é global
                    if (!tabelaSimbolos.contemSimbolo(rotulo)) {
                        System.out.println("  -> ADICIONANDO SÍMBOLO: (" + rotulo + ", " + locctr + ")");
                        tabelaSimbolos.adicionarSimbolo(rotulo, locctr, "LABEL", "DEFINIDO", isGlobal);
                    } else {
                        System.err.println("Erro na linha " + linhaAtual + ": Símbolo duplicado '" + rotulo + "'.");
                    }
                }

                // Lógica de incremento final
                if (instrucaoOuDiretiva != null) {
                    int tamanho = tabelaInstrucao.getInstructionSize(instrucaoOuDiretiva);
                    if (tamanho > 0) {
                        System.out.println("  -> Instrução/Diretiva '" + instrucaoOuDiretiva + "' (tamanho " + tamanho + "). Incrementando locctr.");
                        locctr += tamanho;
                    }
                }

            } // Fim do while

        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico na linha " + linhaAtual + ".");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado na linha " + linhaAtual + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Impressão final da Tabela de Símbolos para verificação.
        System.out.println("\n--- Tabela de Símbolos (Resultado da Primeira Passagem) ---");
        tabelaSimbolos.imprimirTabela();
        System.out.println("---------------------------------------------------------");
    }
}
