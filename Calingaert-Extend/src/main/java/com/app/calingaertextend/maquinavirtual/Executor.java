package com.app.calingaertextend.maquinavirtual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.app.calingaertextend.ViewController;


public class Executor {
        
    private boolean executando;
    private Memoria memoria;
    private Registradores registradores;
    private Pilha pilha;
    private ViewController controller;
    private String label;
    private String opcode;
    private String op1;
    private String op2;
    private String comentario;
    private String linha;

    public Executor (Memoria memoria, Registradores registradores, Pilha pilha) {
        this.memoria = memoria;
        this.registradores = registradores;
        this.pilha = pilha;
        this.executando = true;
        this.label = null;
        this.opcode = null;
        this.op1 = null;
        this.op2 = null;
        this.comentario = null;
        this.linha = null;
    }

    public List<String> gerarListaFormatada() { 
    List<String> resultado = new ArrayList<>();
    int contadorDeLinhas = 1;

    try (InputStream inputStream = getClass().getResourceAsStream("/arquivos/exemplo.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {

        if (inputStream == null) {
            System.out.println("Arquivo não encontrado.");
            return resultado;
        }
        String linhaLida;

        while ((linhaLida =  reader.readLine()) != null) {
            setLinha(linhaLida);
            String[] campos = getLinha().split(","); 

            if (!campos[0].isBlank()) setLabel(campos[0]);
            if (!campos[1].isBlank()) setOpcode(campos[1]);
            if (!campos[2].isBlank()) setOp1(campos[2]);
            if (!campos[3].isBlank()) setOp2(campos[3]);
            if (!campos[4].isBlank()) setComentario(campos[4]);
            
            String linhaArquivoSaida = String.format("Label: %s , Opcode: %s , Op1: %s , Op2: %s , Comentário: %s , Linha: %s",  
            getLabel(), getOpcode(), getOp1(), getOp2(), getComentario(), contadorDeLinhas);

            resultado.add(linhaArquivoSaida);
            contadorDeLinhas++;

        }
            System.out.println("Arquivo escrito!");
        } catch (IOException e) {
        e.printStackTrace();
        }

        return resultado;
    }

    // Provavel que precise ser modificada
    public void executarPasso () throws AcessoIndevidoAMemoriaCheckedException {
        while (executando == true) {
        int pc = registradores.getPC();
        int opcode = memoria.getPosicaoMemoria(pc);
        int op1 = memoria.getPosicaoMemoria(pc+1);
        int op2 = 0;
        registradores.setRI(opcode);
        registradores.setRE(op1);
        
        if (opcode == 13) { 
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

    // Getters e Setters

    public void setController(ViewController controller) {
        this.controller = controller;
    }

    public String getComentario() {
        return comentario;
    }

    public String getLabel() {
        return label;
    }

    public Memoria getMemoria() {
        return memoria;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getOpcode() {
        return opcode;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMemoria(Memoria memoria) {
        this.memoria = memoria;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
