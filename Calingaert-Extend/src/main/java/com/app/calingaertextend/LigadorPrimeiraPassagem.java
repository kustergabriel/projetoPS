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
                continue;
            }

            for (Map.Entry<String, Integer> simboloDefinido : modulo.simbolosGlobaisDefinidos.entrySet()) {
                String nomeSimbolo = simboloDefinido.getKey();
                int enderecoRelativo = simboloDefinido.getValue();

                if (tabelaGlobal.containsKey(nomeSimbolo)) {
                    System.err.println("Erro: Símbolo global já definido: " + nomeSimbolo + " [" + moduloFile.getName() + "]");
                } else {
                    tabelaGlobal.put(nomeSimbolo, baseEndereco + enderecoRelativo);
                }
            }
            baseEndereco += modulo.tamanho;
        }

        System.out.println("--- Tabela Global de Símbolos ---");
        for (Map.Entry<String, Integer> entrada : tabelaGlobal.entrySet()) {
            System.out.println(entrada.getKey() + " -> " + entrada.getValue());
        }
        System.out.println("-----------------------------------");
        
        return tabelaGlobal;
    }
}