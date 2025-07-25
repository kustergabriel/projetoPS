package com.app.calingaertextend.maquinavirtual;

public class ModosEnderecamento {

    public static int resolveOperando (int opcode, int operando, Memoria memoria, boolean permiteIndireto,boolean permiteImediato) throws AcessoIndevidoAMemoriaCheckedException {

        if ((opcode & 0b10000000) != 0) { // bit 128 ligado
            if (!permiteImediato) {
                System.out.println("Modo IMEDIATO nao suportado por essa instrucao");
            } 
            System.out.println("IMEDIATO");
            return operando;
        }

        if (((opcode & 0b00100000) | (opcode & 0b01000000))!= 0) { // bit 32 ou 64 ligados
            if (!permiteIndireto) {
                System.out.println("Modo INDIRETO nao suportado por essa instrucao");
            }
            System.out.println("INDIRETO");
            int endereco = memoria.getPosicaoMemoria(operando);
            return memoria.getPosicaoMemoria(endereco);
        }

        // Direto
        return memoria.getPosicaoMemoria(operando);
    }
}
