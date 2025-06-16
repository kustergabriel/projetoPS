package com.app.calingaertextend;

class Instrucoes {

    public static void executar (int opcode, int op1, int op2, Registradores registrador, Memoria memoria, Pilha pilha) throws AcessoIndevidoAMemoriaCheckedException {

        switch (opcode){

            case 0x00: {
                //BR muda o valor do PC para o endereço que foi informado, tipo PC = op1
                int valorBr = ModosEnderecamento.resolveOperando(opcode, op1, memoria); // Fazer isso aqui para todos...
                registrador.setPC(valorBr);
                break;
            }

            case 0x01: {
                //BRPOS muda o valor do PC caso for maior que zero (ACC > 0)
                int valorBrpos = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() > 0) {
                registrador.setPC(valorBrpos);
                } else registrador.setPC(registrador.getPC() + valorBrpos);
                break;
            }

            case 0x02: {
                //ADD, aqui a gente soma os operandos (ACC = ACC + memoria [op1])
                //registrador.ACC += memoria.ler (op1)
                //ACC = ACC + memoria[op1];
                int valorAdd = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC() + valorAdd);
                registrador.setPC(registrador.getPC() + 2); // Somar mais 2 para a proxima instrucao
                break;
            }

            case 0x03: {
                //LOAD, a gente carrega o operando no ACC (ACC = op1)
                int valorLoad = memoria.getPosicaoMemoria(op1);
                registrador.setACC(valorLoad);
                registrador.setSP(registrador.getPC() + 2);
                break;
            }

            case 0x04: {
                //BRZERO muda o valor do PC caso ACC == 0
                int valorBrzero = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() == 0) {
                    registrador.setPC(valorBrzero);
                } else registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 0x05: {
                //BRNEG muda o PC caso ACC < 0
                int valorNeg = memoria.getPosicaoMemoria(op1);
                if (registrador.getACC() < 0) {
                    registrador.setPC(valorNeg);
                } else registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 0x06: {
                //SUB, aqui a gente subtrai os operandos (ACC = ACC - memoria[op1])
                int valorSub = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC() - valorSub);
                registrador.setPC(registrador.getPC() + 2); // Somar mais 2 para a proxima instrucao
                break;
            }

            case 0x07: {
                //STORE, guarda o ACC em um endereço (OP1 = ACC)
                memoria.setPosicaoMemoria(op1,registrador.getACC());
                registrador.setPC(registrador.getPC() + 2); // Somar mais 2 para a proxima instrucao
                break;
            }

            case 0x08: {
                //WRITE, aqui a gente escreve na saída (output = Op1)
                int valor = memoria.getPosicaoMemoria(op1);
                System.out.println("Saída: " + valor); // Provavelmente aqui vai ter que ter uma area na interface para essas saidas!
                registrador.setPC(registrador.getPC() + 2);
                break;
            }

            case 0x10: {
                //DIVIDE, só divide msm (ACC = ACC/Op1)
                int valordiv = memoria.getPosicaoMemoria(op1);
                registrador.setACC(registrador.getACC( )/ valordiv);
                registrador.setPC (registrador.getPC() +2); //encaixa o ponteiro
                
            break;
            }

            case 0x11: {
                //STOP (para a execução)
                System.exit(0); //BOTO FÉ
            break;
            }

            case 0x12: {
                //READ só lê o a entrada (op1 = input)
                int valorentrada = memoria.getPosicaoMemoria(op1);
                registrador.setACC(valorentrada);
                registrador.setPC(registrador.getPC() +2);
            break;
            }

            case 0x13: {
                //COPY copia de uma op pra outra (op1 <- op2)
                memoria.setPosicaoMemoria(op1, memoria.getPosicaoMemoria(op2));
                registrador.setPC (registrador.getPC() +2);
            break;
            }
            case 0x14: {
                //MULT multiplica os dois valores (ACC = ACC*Op1)
                int valormult = memoria.getPosicaoMemoria(op1);
                registrador.setACC (registrador.getACC()*valormult);
                registrador.setPC(registrador.getPC() + 2);
            break;
            }

            case 0x15: { //CALL chama uma subrotina 
                // Salvo PC no topo da pilha
                pilha.setPosicaoPilha(registrador.getSP(), registrador.getPC());
                //Incremento SP para apontar para o novo topo da pilha
                registrador.setSP(registrador.getSP() + 1);
                //Faço PC receber o desvio
                registrador.setPC(op1);
    
            break;
            }

            case 0x16: { //RET (sai da subrotina)
                // S = SP -1 
                registrador.setSP(registrador.getSP() - 1);
                // PC = pilha[SP]
                registrador.setPC(pilha.getPosicaoPilha(registrador.getSP()));
            break;
            }

            case 0x17: {  //-----------------------------------DUVIDA
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

            case 0x18: { //-----------------------------------DUVIDA

                //POP tira um valor na pilha e coloca no registrador
                // SP = SP -1 
                // registrador = SP

                // Aqui novamente estou supondo que passo na instrução o destino do pop
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