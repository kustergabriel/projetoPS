package com.app.calingaertextend.processadordemacros;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//Responsável pela Tarefa 4: Escrever o código final expandido no arquivo MASMAPRG.ASM.

public class EscritorDeArquivo {

    public void escreverArquivo(String caminhoDoArquivo, List<String> linhasParaEscrever) {
        
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminhoDoArquivo))) {
            

            for (String linha : linhasParaEscrever) {
                escritor.write(linha);
                escritor.newLine();
            }

            System.out.println("\n--- Arquivo final gerado com sucesso! ---");
            System.out.println("Saída salva em: " + caminhoDoArquivo);

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao escrever o arquivo final: " + e.getMessage());
            e.printStackTrace();
        }
    }
}