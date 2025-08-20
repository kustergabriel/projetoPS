
/*O Montador pergunta: "Isto é uma diretiva?". Se sim, ele entra no bloco if. (isDirective)

O Montador verifica: "É a diretiva START?". Se for, ele pega o operando ("MEUPROGRAMA", por exemplo) e o salva em sua variável nomeDoPrograma.

O Montador delega: Ele então chama PseudoInstrucao.processarDiretiva(...) para a sua responsabilidade original: "Quanto espaço isso ocupa?".

O PseudoInstrucao responde: "A diretiva START ocupa 0 de espaço".

O Montador conclui: Ele atualiza seu contadorDePosicao somando 0. */

package com.app.calingaertextend.montador;
import java.util.Set;

public class PseudoInstrucao {
// Classe responsável por processar as diretivas (pseudo-operações) do montador.
    /*Conjunto de diretivas conhecidas para fácil verificação.
    Usamos um Set (conjunto) em vez de uma List ou Array porque a operação de verificar se um item existe (contains()) é top */
    private static final Set<String> DIRETIVAS_CONHECIDAS = Set.of(
        "START", "END", "INTDEF", "INTUSE", "CONST", "SPACE", "STACK"
    );

    /*  Pode chamar este método pra saber se oq tu ta lendo é uma diretiva
        reotrna true == diretiva; false == ~diretiva
     */
    public static boolean isDirective(String mnemonico) {
        return DIRETIVAS_CONHECIDAS.contains(mnemonico.toUpperCase());
    }

    /*
    Processa uma diretiva e retorna o número de palavras de memória que ela ocupa.
    Parametros: Diretiva (O nome da diretiva a ser processada).
                Os operandos associados à diretiva.
    Return: O número de palavras de memória ocupadas pela diretiva.
     */
    public int processarDiretiva(String diretiva, String operandos) {
        if (!isDirective(diretiva)) {
            // Se não for uma diretiva, lança uma exceção.
            // Isso indica um erro de lógica no chamador (RESOLVAM SEUS BO's MAN).
            throw new IllegalArgumentException("'" + diretiva + "' não é uma diretiva válida.");
        }

        switch (diretiva.toUpperCase()) {
            case "START": {
                System.out.println("Processando START com operandos: " + operandos);  // Da pra remover, é pra debugar esse SOUT
                return 0;
            }
            case "END": {
                System.out.println("Processando END."); // Da pra remover, é pra debugar esse SOUT
                return 0; 
            }    
            case "CONST": {
                System.out.println("Processando CONST com valor: " + operandos);
                return 1;
            }
            case "SPACE": {
                System.out.println("Processando SPACE com operandos: " + operandos);
                try {
                    if (operandos == null || operandos.trim().isEmpty()) {
                        return 1;
                    } else {
                        return Integer.parseInt(operandos.trim());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Erro: SPACE precisa de um número inteiro.");
                    return 1; // Se colocar texto no space vai da merda e retornar 1 xd
                }
            }
            case "STACK": {
                System.out.println("Processando STACK com tamanho: " + operandos);
                return 0;
            }
            case "INTDEF": {
                System.out.println("Processando INTDEF para o símbolo: " + operandos);
                return 0;
            }
            case "INTUSE": {
                System.out.println("Processando INTUSE para o símbolo: " + operandos);
                return 0;
            }    
            default:
                return 0; // Não deve acontecer devido à verificação isDirective()
            }
    }
}