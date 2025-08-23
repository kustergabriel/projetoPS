package com.app.calingaertextend.maquinavirtual;

import com.app.calingaertextend.ViewController;
import javafx.application.Platform;
import java.util.List;

public class Executor {

    private boolean executando;
    private final Memoria memoria;
    private final Registradores registradores;
    private final Pilha pilha;
    private ViewController controller;

    public Executor(Memoria memoria, Registradores registradores, Pilha pilha) {
        this.memoria = memoria;
        this.registradores = registradores;
        this.pilha = pilha;
        this.executando = true;
    }

    public void executarPasso() throws AcessoIndevidoAMemoriaCheckedException {
        while (executando) {
            int pc = registradores.getPC();
            int opcode = memoria.getPosicaoMemoria(pc);

            registradores.setRI(opcode);
            
            if (opcode == 11) {
                Instrucoes.executar(opcode, 0, 0, registradores, memoria, this, pilha);
                continue; 
            }

            int op1 = 0;
            int op2 = 0;

            if (opcode == 13) { 
                op1 = memoria.getPosicaoMemoria(pc + 1);
                op2 = memoria.getPosicaoMemoria(pc + 2);
            } else if (opcode != 16) { 
                op1 = memoria.getPosicaoMemoria(pc + 1);
            }

            registradores.setRE(op1);
            Instrucoes.executar(opcode, op1, op2, registradores, memoria, this, pilha);
        }

        Platform.runLater(() -> {
            controller.atualizarTabela(registradores);
            controller.atualizarTabelaMemoria(memoria.getMemoria());
        });
    }

    public void pararExecucao() {
        this.executando = false;
    }

    public void setController(ViewController controller) {
        this.controller = controller;
    }
    
    public Memoria getMemoria() { 
        return memoria; 
    }
    
}