package com.app.calingaertextend;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.io.IOException;
import java.util.List;
import com.app.calingaertextend.montador.*;
import com.app.calingaertextend.processadordemacros.*;
import com.app.calingaertextend.maquinavirtual.*;


public class Main extends Application {

    public static Memoria memoria;
    public static Registradores registradores;
    public static Pilha pilha;
    public static Executor executor;
    public static ViewController controller;
    public static Leitor leitor;
    public static TabelaDeMacros tabela;
    public static ExpansorDeMacros expansorDeMacros;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Calingaert Extend");
        stage.show();

        // Controller da Interface
        controller = fxmlLoader.getController();
        controller.setStage(stage);

        memoria = new Memoria(12);
        registradores = new Registradores();
        pilha = new Pilha(12);
        executor = new Executor(memoria, registradores, pilha);

        // Passar o controller para o executor atualizar os dados no futuro
        executor.setController(controller);

        // Iniciar tabelas
        controller.atualizarTabela(registradores);
        controller.atualizarTabelaMemoria(memoria.getMemoria());

        System.out.println(executor.gerarListaFormatada()); // Retorna uma lista, adicionei a variavel LINHA tambem, nao é necessario usar ela

        String arquivoFonteOriginal = "Calingaert-Extend/src/main/java/com/app/calingaertextend/montador/teste3.txt";
        String arquivoExpandido = "MASMAPRG.txt"; // Saída do processador de macros, entrada do montador
        String arquivoObjetoMontado = "Calingaert-Extend/src/main/java/com/app/calingaertextend/montador/saida.txt"; // Saída do montador
        String arquivoExecutavel = "programa.hpx"; // Saída do ligador

        // 1. Processador de Macros
        System.out.println("\n--- Iniciando Processador de Macros ---");
        leitor = new Leitor();
        leitor.lerArquivo(arquivoFonteOriginal);
        List<ListaAsm> linhasClassificadas = leitor.getLinhasFeitas();

        tabela = new TabelaDeMacros();
        tabela.processarMacros(linhasClassificadas);

        expansorDeMacros = new ExpansorDeMacros(tabela);
        List<String> codigoFinalExpandido = expansorDeMacros.expandir(linhasClassificadas);
        
        EscritorDeArquivo escritorMacros = new EscritorDeArquivo();
        escritorMacros.escreverArquivo(arquivoExpandido, codigoFinalExpandido);
        System.out.println("--- Processador de Macros Concluído ---");

        // 2. Montador (Primeira e Segunda Passagem)
        System.out.println("\n--- Iniciando Montador ---");
        PrimeiraPassagem pp = new PrimeiraPassagem();
        SegundaPassagem sp = new SegundaPassagem();
        TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
        TabelaInstrucao tabelaInstrucao = new TabelaInstrucao();

        pp.primeirapassagem(arquivoExpandido, tabelaSimbolos, tabelaInstrucao);
        sp.segundapassagem(arquivoExpandido, arquivoObjetoMontado, tabelaSimbolos, tabelaInstrucao);
        controller.atualizarTabelaSimbolos(tabelaSimbolos); // Atualiza a UI com a tabela de símbolos
        System.out.println("--- Montador Concluído ---");

        // 3. Ligador (Primeira e Segunda Passagem)
        System.out.println("\n--- Iniciando Ligador ---");
        // Para este exemplo, assumimos que 'saida.txt' é o único módulo objeto.
        // Em um cenário real, você teria vários arquivos .obj
        List<File> modulosObjeto = Arrays.asList(new File(arquivoObjetoMontado));

        LigadorPrimeiraPassagem ligadorPP = new LigadorPrimeiraPassagem();
        Map<String, Integer> tabelaGlobal = ligadorPP.executarPassagem(modulosObjeto);

        LigadorSegundaPassagem ligadorSP = new LigadorSegundaPassagem(tabelaGlobal);
        ligadorSP.executarPassagem(modulosObjeto, arquivoExecutavel);
        System.out.println("--- Ligador Concluído ---");

        // 4. Carregador e Execução na Máquina Virtual
        System.out.println("\n--- Iniciando Carregador e Execução ---");
        LeitorObjeto leitorExecutavel = new LeitorObjeto();
        LeitorObjeto.ModuloObjeto programaCarregado = leitorExecutavel.lerArquivoObjeto(arquivoExecutavel);

        if (programaCarregado != null) {
            // Carregar o código objeto na memória da máquina virtual
            // O formato do arquivo .hpx (saida.txt) é crucial aqui.
            // Assumindo que cada linha em codigoObjeto é uma instrução/dado
            int enderecoAtual = 0; // Endereço de carga, pode ser 0 ou um endereço base definido
            for (String linhaCodigo : programaCarregado.codigoObjeto) {
                // Aqui você precisaria parsear a linha (opcode e operandos)
                // e colocar na memória. O formato atual de saida.txt é "OPCODE OPERANDO1 OPERANDO2"
                String[] partesCodigo = linhaCodigo.split("\\s+");
                try {
                    // Coloca o opcode
                    memoria.setPosicaoMemoria(enderecoAtual++, Integer.parseInt(partesCodigo[0]));
                    // Coloca os operandos, se existirem
                    for (int i = 1; i < partesCodigo.length; i++) {
                        memoria.setPosicaoMemoria(enderecoAtual++, Integer.parseInt(partesCodigo[i]));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao parsear código objeto: " + linhaCodigo);
                } catch (AcessoIndevidoAMemoriaCheckedException e) {
                    System.err.println("Erro de acesso à memória ao carregar programa: " + e.getMessage());
                }
            }

            // Definir o PC inicial para o endereço de execução do programa
            // O PDF menciona que o ligador informa o endereço inicial para execução.
            // No seu LeitorObjeto.ModuloObjeto, você tem 'enderecoInicial'.
            // Se o programa for carregado a partir do endereço 0 da memória, o PC inicial será o 'enderecoInicial' do módulo.
            registradores.setPC(programaCarregado.enderecoInicial); 
            
            // Atualizar a UI com a memória carregada
            controller.atualizarTabelaMemoria(memoria.getMemoria());
            controller.atualizarTabela(registradores);

            // Executar o programa
            try {
                executor.executarPasso(); // Isso executará o programa carregado
            } catch (AcessoIndevidoAMemoriaCheckedException e) {
                System.err.println("Erro durante a execução do programa: " + e.getMessage());
            }
        } else {
            System.err.println("Não foi possível carregar o programa executável.");
        }
        System.out.println("--- Carregador e Execução Concluídos ---");
    }

    public static void main(String[] args) {
        launch();
    }
}
