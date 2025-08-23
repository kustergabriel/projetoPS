package com.app.calingaertextend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeitorObjeto {

    public static class ModuloObjeto {
        public int tamanho;
        public int enderecoInicial;
        public Map<String, Integer> simbolosGlobaisDefinidos = new HashMap<>();
        public List<String> simbolosExternos = new ArrayList<>();
        public List<Integer> mapaDeRelocacao = new ArrayList<>();
        public List<String> codigoObjeto = new ArrayList<>();
    }

    public ModuloObjeto lerArquivoObjeto(String caminho) {
        ModuloObjeto modulo = new ModuloObjeto();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            String secaoAtual = "";

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                
                // Identifica o início de cada seção
                if (linha.startsWith("TAMANHO:")) {
                    modulo.tamanho = Integer.parseInt(linha.split(":")[1].trim());
                    continue;
                }
                if (linha.startsWith("ENDERECO_INICIAL:")) {
                    modulo.enderecoInicial = Integer.parseInt(linha.split(":")[1].trim());
                    continue;
                }
                if (linha.equals("--- GLOBAIS ---")) {
                    secaoAtual = "GLOBAIS";
                    continue;
                }
                if (linha.equals("--- EXTERNOS ---")) {
                    secaoAtual = "EXTERNOS";
                    continue;
                }
                if (linha.equals("--- RELOCACAO ---")) {
                    secaoAtual = "RELOCACAO";
                    continue;
                }
                if (linha.equals("--- CODIGO ---")) {
                    secaoAtual = "CODIGO";
                    continue;
                }

                switch (secaoAtual) {
                    case "GLOBAIS":
                        String[] partesGlobais = linha.split("\\s+");
                        if (partesGlobais.length >= 2) {
                            modulo.simbolosGlobaisDefinidos.put(partesGlobais[0], Integer.parseInt(partesGlobais[1]));
                        }
                        break;
                    case "EXTERNOS":
                        modulo.simbolosExternos.add(linha);
                        break;
                    case "RELOCACAO":
                        String[] partesReloc = linha.split(",");
                        for (String pos : partesReloc) {
                            if (!pos.isEmpty()) {
                                modulo.mapaDeRelocacao.add(Integer.parseInt(pos));
                            }
                        }
                        break;
                    case "CODIGO":
                        modulo.codigoObjeto.add(linha);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo objeto: " + caminho + " -> " + e.getMessage());
            return null;
        }
        return modulo;
    }
}