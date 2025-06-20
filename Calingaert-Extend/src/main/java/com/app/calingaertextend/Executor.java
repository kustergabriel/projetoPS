package com.app.calingaertextend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Executor {
        
    private boolean executando;
    private Memoria memoria;
    private Registradores registradores;
    private Pilha pilha;
    private ViewController controller;

    public Executor (Memoria memoria, Registradores registradores, Pilha pilha) {
        this.memoria = memoria;
        this.registradores = registradores;
        this.pilha = pilha;
        this.executando = true;

    }

    public void carregarPrograma() {
    try (InputStream inputStream = getClass().getResourceAsStream("/arquivos/exemplo.txt")) {
        if (inputStream == null) {
            System.out.println("Arquivo não encontrado.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String linha;
            int endereco = 0;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] numeros = linha.split(" "); // separa por espaços
                for (String numStr : numeros) {
                    int valor = Integer.parseInt(numStr);
                    memoria.setPosicaoMemoria(endereco, valor);
                    endereco++;
                }
            }
            System.out.println("Programa carregado na memória.");
        }
    } catch (IOException | AcessoIndevidoAMemoriaCheckedException e) {
        e.printStackTrace();
        }
    }

    public void executarPasso () throws AcessoIndevidoAMemoriaCheckedException {
        while (executando == true) {
        int pc = registradores.getPC();
        int opcode = memoria.getPosicaoMemoria(pc);
        int op1 = memoria.getPosicaoMemoria(pc+1);
        int op2 = 0;

        registradores.setRI(opcode);
        registradores.setRE(op1);
        
        if (opcode == 0x13) { // Copy precisa de 2 operandos
            op2 = memoria.getPosicaoMemoria(pc + 2); 
        }

        Instrucoes.executar(opcode, op1, op2, registradores, memoria, this, pilha);

        }
        controller.atualizarTabela(registradores);
        controller.atualizarTabelaMemoria(memoria.getMemoria());
    }
        
    public void pararExecucao () {
        this.executando = false;
    }

    public void setController(ViewController controller) {
        this.controller = controller;
    }

}
