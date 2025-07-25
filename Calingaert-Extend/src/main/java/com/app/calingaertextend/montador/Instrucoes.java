package com.app.calingaertextend.montador;

import com.app.calingaertextend.maquinavirtual.Registradores;
import com.app.calingaertextend.maquinavirtual.Memoria;
import com.app.calingaertextend.maquinavirtual.Executor;
import com.app.calingaertextend.maquinavirtual.Pilha;
import com.app.calingaertextend.maquinavirtual.ModosEnderecamento;
import com.app.calingaertextend.maquinavirtual.AcessoIndevidoAMemoriaCheckedException;

class Instrucoes {

    public static void executar (int opcode, int op1, int op2, Registradores registrador, Memoria memoria, Executor executor, Pilha pilha) throws AcessoIndevidoAMemoriaCheckedException {

        int opcodeBase = opcode & 0b00011111; // And bit a bit 

        switch (opcodeBase){
            
            case 0: { // TESTADO
                //BR muda o valor do PC para o endereço que foi informado, tipo PC = op1
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                registrador.setPC(valor);
                break;
            }

            case 1: { // TESTADO
                //BRPOS muda o valor do PC caso for maior que zero (ACC > 0)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                if (registrador.getACC() > 0) {
                    registrador.setPC(valor);
                    System.out.println("DEBUG --- BRPOS ----");
                    System.out.println("PC: " + registrador.getPC());
                    System.out.println("Valor: " + valor);
                    System.out.println("op1: " + op1);
                } else {
                    registrador.setPC(registrador.getPC() + 2); // Só avança pra próxima instrução
                }       
            break;
            }

            case 2: { // TESTADO
                //ADD, aqui a gente soma os operandos (ACC = ACC + memoria [op1])
                //registrador.ACC += memoria.ler (op1)
                //ACC = ACC + memoria[op1];
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                System.out.println("DEGUB ---- ADD ----");
                System.out.println("OP1: " + op1);
                System.out.println("Valor: " + valor);
                registrador.setACC(registrador.getACC() + valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
                }

            case 3: { // TESTADO
                //LOAD, a gente carrega o operando no ACC (ACC = op1)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                System.out.println("DEGUB ---- LOAD ----");
                System.out.println("OP1: " + op1);                
                System.out.println("Valor: " + valor);
                System.out.println("PC: " + registrador.getPC());
                registrador.setACC(valor);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 4: { // TESTADO
                //BRZERO muda o valor do PC caso ACC == 0
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                if (registrador.getACC() == 0) {
                    registrador.setPC(valor);
                } else {
                    registrador.setPC(registrador.getPC() + 2);
                }
                break;
            }

            case 5: { // TESTADO
                //BRNEG muda o PC caso ACC < 0
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                if (registrador.getACC() < 0) {
                    registrador.setPC(valor);
                } else {
                    registrador.setPC(registrador.getPC() + 2);
                }
                break;
            }

            case 6: { // TESTADO
                //SUB, aqui a gente subtrai os operandos (ACC = ACC - memoria[op1])
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true); 
                registrador.setACC(registrador.getACC() - valor);
                registrador.setPC(registrador.getPC() + 2); 
                break;
            }

            case 7: { // TESTADO
                //STORE, guarda o ACC em um endereço (OP1 = ACC)
                int destino;
                if ((opcode & 0b00100000) != 0) { // Bit 5 → indireto
                    destino = memoria.getPosicaoMemoria(op1);
                } else {
                    destino = op1;
                }

                System.out.println("DEBUG ---- STORE ----");
                System.out.println("Destino (endereço): " + destino);
                System.out.println("ACC: " + registrador.getACC());
                System.out.println("PC: " + registrador.getPC());

                memoria.setPosicaoMemoria(destino, registrador.getACC());

                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 8: { // TESTADO
                //WRITE, aqui a gente escreve na saída (output = Op1)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true);
                System.out.println("Saída: " + valor); // IMPRIMIR NA INTERFACE!!!!!
                System.out.println("PC: " + registrador.getPC()); // APENAS DEBUG

                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 10: {
                //DIVIDE, só divide msm (ACC = ACC/Op1)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true); 
                registrador.setACC(registrador.getACC( ) / valor);
                registrador.setPC (registrador.getPC() +2); //encaixa o ponteiro
            break;
            }

            case 11: { // TESTADO
                // Finalizar programa
                System.out.println("Programa finalizado com sucesso.");
                executor.pararExecucao(); 
                break;
            }

            case 12: { // ACHO QUE NAO ESTA CERTO
                //READ só lê o a entrada (op1 = input)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                registrador.setACC(valor);
                registrador.setPC(registrador.getPC() +2);
            break;
            }

            case 13: {
                //COPY copia de uma op pra outra (op1 = op2)
                int destino;
                if ((opcode & 0b00100000) != 0) {
                    destino = memoria.getPosicaoMemoria(op1);
                } else {
                    destino = op1;
                }
                int valor2 = ModosEnderecamento.resolveOperando(opcode, op2, memoria,true,true); 
                memoria.setPosicaoMemoria(destino, valor2);
                registrador.setPC (registrador.getPC() + 3);
            break;
            }
            
            case 14: {
                //MULT multiplica os dois valores (ACC = ACC*Op1)
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,true); 
                registrador.setACC (registrador.getACC()*valor);
                registrador.setPC(registrador.getPC() + 2);
            break;
            }

            case 15: {
                // Salvo PC no topo da pilha
                int valor = ModosEnderecamento.resolveOperando(opcode, op1, memoria,true,false); 
                pilha.setPosicaoPilha(registrador.getSP(), registrador.getPC());
                //Incremento SP para apontar para o novo topo da pilha
                registrador.setSP(registrador.getSP() + 1);
                //Faço PC receber o desvio
                registrador.setPC(valor);
            break;
            }

            case 16: {
                //RET (sai d subrotina e volta pro código)
                // S = SP -1 
                registrador.setSP(registrador.getSP() - 1);
                // PC = pilha[SP]
                registrador.setPC(pilha.getPosicaoPilha(registrador.getSP()));
            break;
            }

            case 17: {
                //PUSH coloca um valor na pilha e ajeita o ponteiro
                // SP = registrador
                // SP = SP + 1
                int aux = registrador.getSP()  + 1;  //SP +=1
                pilha.setPosicaoPilha(aux, op1);

                registrador.setSP(registrador.getSP() + 1); // Atualiza SP verdadeiro
                registrador.setPC(registrador.getPC() + 2);
                //DUVIDA!!!!!
                //PUSH recebe 1 registrador? O valor fica no op1?
            break;
            }

            case 18: {
                int valorPop = pilha.getPosicaoPilha(registrador.getSP()); //Pego o valor da pilha
                
                switch (op1) {
                    case 0: registrador.setR0(valorPop);
                        break;
                    case 1: registrador.setR1(valorPop);
                        break;
                    case 2: registrador.setACC(valorPop);
                        break;
                    default: 
                        System.err.println("Registrador inválido na instrução POP: " + op1);
                }
    
                registrador.setSP(registrador.getSP() - 1); // Atualiza SP verdadeiro
                registrador.setPC(registrador.getPC() + 2); // Atualiza PC

            break;
            }
        }
    }
}