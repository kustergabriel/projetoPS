package com.app.calingaertextend.maquinavirtual;

class Instrucoes {
    public static void executar (int opcode, int op1, int op2, Registradores registrador, Memoria memoria, Executor executor, Pilha pilha) throws AcessoIndevidoAMemoriaCheckedException {

        // A máscara de bits extrai o opcode base, ignorando os modos de endereçamento.
        int opcodeBase = opcode & 0b00011111; 

        // *** CORREÇÃO CRÍTICA APLICADA AQUI ***
        // O switch agora usa o 'opcodeBase' para a comparação.
        switch (opcodeBase){
            case 0: { // BR
                int valor = op1;
                registrador.setPC(valor);
                break;
            }
            case 1: { // BRPOS
                int valor = op1;
                if (registrador.getACC() > 0) {
                    registrador.setPC(valor);
                } else {
                    registrador.setPC(registrador.getPC() + 2);
                }
                break;
            }
            case 2: { // ADD
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                registrador.setACC(registrador.getACC() + valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 3: { // LOAD
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                registrador.setACC(valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 4: { // BRZERO
                int valor = op1;
                if (registrador.getACC() == 0) {
                    registrador.setPC(valor);
                } else {
                    registrador.setPC(registrador.getPC() + 2);
                }
                break;
            }
            case 5: { // BRNEG
                int valor = op1;
                if (registrador.getACC() < 0) {
                    registrador.setPC(valor);
                } else {
                    registrador.setPC(registrador.getPC() + 2);
                }
                break;
            }
            case 6: { // SUB
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                registrador.setACC(registrador.getACC() - valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 7: { // STORE
                memoria.setPosicaoMemoria(op1, registrador.getACC());
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 8: { // WRITE
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                System.out.println("Saída: " + valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 10: { // DIVIDE
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                registrador.setACC(registrador.getACC() / valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 11: { // STOP
                System.out.println("Programa finalizado com sucesso.");
                executor.pararExecucao();
                registrador.setPC(registrador.getPC() + 1);
                break;
            }
            case 12: { // READ
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            // Em Instrucoes.java
            case 13: { // COPY
                // O Executor passa op1 como o primeiro operando e op2 como o segundo.
                // Para COPY, a sintaxe é COPY ORIGEM DESTINO.
                // op1 = endereço de ORIGEM
                // op2 = endereço de DESTINO
                int valorOrigem = ModosEnderecamento.resolveOperando(opcode, op1, memoria, true, true);
                memoria.setPosicaoMemoria(op2, valorOrigem); // memoria[destino] = valor da origem
                registrador.setPC(registrador.getPC() + 3);
                break;
            }
            case 14: { // MULT
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                registrador.setACC(registrador.getACC() * valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 15: { // CALL
                int valor = op1;
                pilha.setPosicaoPilha(registrador.getSP(), registrador.getPC() + 2);
                registrador.setSP(registrador.getSP() + 1);
                registrador.setPC(valor);
                break;
            }
            case 16: { // RET
                registrador.setSP(registrador.getSP() - 1);
                registrador.setPC(pilha.getPosicaoPilha(registrador.getSP()));
                break;
            }
            case 17: { // PUSH
                // A implementação de PUSH pode variar, mas assumindo que empurra o valor do operando
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria, true, true);
                pilha.setPosicaoPilha(registrador.getSP(), valor);
                registrador.setSP(registrador.getSP() + 1);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            case 18: { // POP
                registrador.setSP(registrador.getSP() - 1);
                int valorPop = pilha.getPosicaoPilha(registrador.getSP());
                memoria.setPosicaoMemoria(op1, valorPop); // POP para um endereço de memória
                registrador.setPC(registrador.getPC() + 2);
                break;
            }
            default:
                 System.err.println("Opcode base desconhecido encontrado: " + opcodeBase + ". Execução interrompida.");
                 executor.pararExecucao();
                 break;
        }
    }
}