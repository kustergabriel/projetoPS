package com.app.calingaertextend;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LigadorPrimeiraPassagem {
    private Map<String, Integer> tabelaGlobal = new HashMap<>();

    public Map<String, Integer> executarPassagem(List<File> modulos) {
        int baseEndereco = 0;

        for (File moduloFile : modulos) {
            LeitorObjeto leitor = new LeitorObjeto();
            LeitorObjeto.ModuloObjeto modulo = leitor.lerArquivoObjeto(moduloFile.getPath());

            if (modulo == null) {
                // Erro na leitura do arquivo, pula para o próximo
                continue;
            }

            // Itera sobre os símbolos globais definidos neste módulo
            for (Map.Entry<String, Integer> simboloDefinido : modulo.simbolosGlobaisDefinidos.entrySet()) {
                String nomeSimbolo = simboloDefinido.getKey();
                int enderecoRelativo = simboloDefinido.getValue();

                // Verifica se o símbolo já existe na tabela global
                if (tabelaGlobal.containsKey(nomeSimbolo)) {
                    // Erro: Símbolo global já definido
                    // O nome do módulo anterior não está disponível aqui, mas podemos pegar o nome do arquivo.
                    System.err.println("Erro: Símbolo global já definido: " + nomeSimbolo + " [" + moduloFile.getName() + "]");
                    // Poderíamos lançar uma exceção ou parar a execução aqui
                } else {
                    // Adiciona o símbolo à tabela global com seu endereço real
                    tabelaGlobal.put(nomeSimbolo, baseEndereco + enderecoRelativo);
                }
            }
            // A base de endereço para o próximo módulo é a base atual + o tamanho do módulo recém-processado.
            baseEndereco += modulo.tamanho;
        }

        // Imprime a Tabela Global
        System.out.println("--- Tabela Global de Símbolos ---");
        for (Map.Entry<String, Integer> entrada : tabelaGlobal.entrySet()) {
            System.out.println(entrada.getKey() + " -> " + entrada.getValue());
        }
        System.out.println("-----------------------------------");
        
        return tabelaGlobal;
    }
}