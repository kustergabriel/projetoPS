package com.app.calingaertextend;


class Instrucoes {

    public static void executar (int opcode, int op1, int op2, Registradores registrador, Memoria memoria, Executor executor, Pilha pilha) throws AcessoIndevidoAMemoriaCheckedException {

        switch (opcode){

            case 0: {
                //BR muda o valor do PC para o endereço que foi informado, tipo PC = op1
                int valorBr = ModosEnderecamento.resolveOperando(opcode, op1, memoria); // Fazer isso aqui para todos...
                registrador.setPC(valorBr);
                break;
            }

            case 1: {
                //BRPOS muda o valor do PC caso for maior que zero (ACC > 0)
                int valorBrpos = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() > 0) {
                registrador.setPC(valorBrpos);
                } else registrador.setPC(registrador.getPC() + valorBrpos);
                break;
            }

            case 2: {
                //ADD, aqui a gente soma os operandos (ACC = ACC + memoria [op1])
                //registrador.ACC += memoria.ler (op1)
                //ACC = ACC + memoria[op1];
                int valorAdd = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC() + valorAdd);
                registrador.setPC(registrador.getPC() + 2);
                break;
                }

            case 3: {
                //LOAD, a gente carrega o operando no ACC (ACC = op1)
                int valorLoad = memoria.getPosicaoMemoria(op1);
                registrador.setACC(valorLoad);
                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 4: {
                //BRZERO muda o valor do PC caso ACC == 0
                int valorBrzero = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() == 0) {
                    registrador.setPC(valorBrzero);
                } else registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 5: {
                //BRNEG muda o PC caso ACC < 0
                int valorNeg = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() < 0) {
                    registrador.setPC(valorNeg);
                } else registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 6: {
                //SUB, aqui a gente subtrai os operandos (ACC = ACC - memoria[op1])
                int valorSub = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC() - valorSub);
                registrador.setPC(registrador.getPC() + 2); // Somar mais 2 para a proxima instrucao
                break;
            }

            case 7: {
                //STORE, guarda o ACC em um endereço (OP1 = ACC)
                memoria.setPosicaoMemoria(op1,registrador.getACC());
                registrador.setPC(registrador.getPC() + 2); // Somar mais 2 para a proxima instrucao
                break;
            }

            case 8: {
                //WRITE, aqui a gente escreve na saída (output = Op1)
                int valor = memoria.getPosicaoMemoria(op1);
                System.out.println("Saída: " + valor); // Provavelmente aqui vai ter que ter uma area na interface para essas saidas!
                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 10: {
                //DIVIDE, só divide msm (ACC = ACC/Op1)
                int valordiv = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC( )/ valordiv);
                registrador.setPC (registrador.getPC() +2); //encaixa o ponteiro
            break;
            }

            case 11: {
                // Finalizar programa
                System.out.println("Programa finalizado com sucesso.");
                executor.pararExecucao(); 
                break;
            }

            case 12: {
                //READ só lê o a entrada (op1 = input)
                int valorentrada = memoria.getPosicaoMemoria(op1);
                registrador.setACC(valorentrada);
                registrador.setPC(registrador.getPC() +2);
            break;
            }

            case 13: {
                //COPY copia de uma op pra outra (op1 = op2)
                memoria.setPosicaoMemoria(op1, memoria.getPosicaoMemoria(op2));
                registrador.setPC (registrador.getPC() +2);
            break;
            }
            
            case 14: {
                //MULT multiplica os dois valores (ACC = ACC*Op1)
                int valormult = memoria.getPosicaoMemoria(op1);
                registrador.setACC (registrador.getACC()*valormult);
                registrador.setPC(registrador.getPC() + 2);
            break;
            }

            case 15: {
                // Salvo PC no topo da pilha
                pilha.setPosicaoPilha(registrador.getSP(), registrador.getPC());
                //Incremento SP para apontar para o novo topo da pilha
                registrador.setSP(registrador.getSP() + 1);
                //Faço PC receber o desvio
                registrador.setPC(op1);
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