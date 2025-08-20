package com.app.calingaertextend.montador;


import java.util.HashMap;
import java.util.Map;

public class TabelaInstrucao {
    // Usar mapas estáticos é mais eficiente, pois são criados apenas uma vez.
    private static final Map<String, Integer> opcodes = new HashMap<>();
    private static final Map<String, Integer> instructionSizes = new HashMap<>();

    // Bloco estático para preencher os mapas quando a classe é carregada
    static {
        // Mapeamento de NOME DA INSTRUÇÃO para OPCODE BASE
        // e de NOME DA INSTRUÇÃO para TAMANHO (em "palavras" de memória)

        // Instruções de 1 operando (tamanho 2: opcode + operando)
        opcodes.put("BR", 0);      instructionSizes.put("BR", 2);
        opcodes.put("BRPOS", 1);   instructionSizes.put("BRPOS", 2);
        opcodes.put("ADD", 2);     instructionSizes.put("ADD", 2);
        opcodes.put("LOAD", 3);    instructionSizes.put("LOAD", 2);
        opcodes.put("BRZERO", 4);  instructionSizes.put("BRZERO", 2);
        opcodes.put("BRNEG", 5);   instructionSizes.put("BRNEG", 2);
        opcodes.put("SUB", 6);     instructionSizes.put("SUB", 2);
        opcodes.put("STORE", 7);   instructionSizes.put("STORE", 2);
        opcodes.put("WRITE", 8);   instructionSizes.put("WRITE", 2);
        opcodes.put("SHIFTL", 9);  instructionSizes.put("SHIFTL", 2);
        opcodes.put("DIV", 10);    instructionSizes.put("DIV", 2);
        opcodes.put("READ", 12);   instructionSizes.put("READ", 2);
        opcodes.put("MULT", 14);   instructionSizes.put("MULT", 2);
        opcodes.put("CALL", 15);   instructionSizes.put("CALL", 2);
        opcodes.put("PUSH", 17);   instructionSizes.put("PUSH", 2);
        opcodes.put("POP", 18);    instructionSizes.put("POP", 2);
        opcodes.put("SHIFTR", 19); instructionSizes.put("SHIFTR", 2);

        // Instruções de 2 operandos (tamanho 3: opcode + op1 + op2)
        opcodes.put("COPY", 13);   instructionSizes.put("COPY", 3);

        // Instruções de 0 operandos (tamanho 1: apenas opcode)
        opcodes.put("STOP", 11);   instructionSizes.put("STOP", 1);
        opcodes.put("RET", 16);    instructionSizes.put("RET", 1);

        //  CORREÇÃO: Diretivas que ocupam 1 palavra de memória
        instructionSizes.put("SPACE", 1);
        instructionSizes.put("CONST", 1);

        // Diretivas (precisam estar aqui para o método 'contains')
        opcodes.put("START", -1);
        opcodes.put("END", -1);
        opcodes.put("SPACE", -1);
        opcodes.put("CONST", -1); // Adicionada a diretiva CONST
        opcodes.put("MACRO", -1);
        opcodes.put("MEND", -1);
        opcodes.put("SCALE", -1);
        opcodes.put("DISCR", -1);
        opcodes.put("MULTSC", -1);
        opcodes.put("DIVSC", -1);
    }

    /**
     * Verifica se uma palavra é uma instrução ou diretiva conhecida.
     */
    public boolean contains(String nome) {
        if (nome == null) return false;
        return opcodes.containsKey(nome.toUpperCase());
    }

    /**
     * Retorna o opcode base para uma instrução.
     * Retorna -1 para diretivas ou se não for encontrada.
     */
    public Integer getOpcode(String operacao) {
        if (operacao == null) return -1;
        return opcodes.getOrDefault(operacao.toUpperCase(), -1);
    }

    /**
     * ✅ MÉTODO ATUALIZADO E SIMPLIFICADO
     * Retorna o tamanho em "palavras" de uma instrução ou diretiva.
     * É usado pela Primeira Passagem para incrementar o locctr corretamente.
     * @param instrucao O nome da instrução ou diretiva (ex: "LOAD", "SPACE", "CONST").
     * @return O tamanho em palavras de memória (1, 2, 3) ou 0 se não ocupar espaço.
     */
    public int getInstructionSize(String instrucao) {
        if (instrucao == null) return 0;
        
        // Busca o tamanho no mapa 'instructionSizes'.
        // Retorna 0 se não for uma instrução que ocupe espaço (ex: START, END).
        return instructionSizes.getOrDefault(instrucao.toUpperCase(), 0);
    }
}