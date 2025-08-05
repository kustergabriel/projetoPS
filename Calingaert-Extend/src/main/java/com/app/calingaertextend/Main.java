package com.app.calingaertextend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import com.app.calingaertextend.maquinavirtual.Executor;
import com.app.calingaertextend.maquinavirtual.Memoria;
import com.app.calingaertextend.maquinavirtual.Pilha;
import com.app.calingaertextend.maquinavirtual.Registradores;
import com.app.calingaertextend.montador.PrimeiraPassagem;
import com.app.calingaertextend.montador.SegundaPassagem;
import com.app.calingaertextend.montador.TabelaDeSimbolos;
import com.app.calingaertextend.montador.TabelaInstrucao;
import com.app.calingaertextend.processadordemacros.EscritorDeArquivo;
import com.app.calingaertextend.processadordemacros.ExpansorDeMacros;
import com.app.calingaertextend.processadordemacros.Leitor;
import com.app.calingaertextend.processadordemacros.ListaAsm;
import com.app.calingaertextend.processadordemacros.TabelaDeMacros;

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

        String arquivoEntrada2 = "MASMAPRG.txt";
        String arquivoEntrada = "Calingaert-Extend/src/main/java/com/app/calingaertextend/montador/teste3.txt";
        String arquivoSaida = "Calingaert-Extend/src/main/java/com/app/calingaertextend/montador/saida.txt";   

        // Processador de macros
        leitor = new Leitor();
        leitor.lerArquivo(arquivoEntrada);

        List<ListaAsm> linhasClassificadas = leitor.getLinhasFeitas();

        tabela = new TabelaDeMacros();
        tabela.processarMacros(linhasClassificadas);

        expansorDeMacros = new ExpansorDeMacros(tabela);

        List<String> codigoFinal = expansorDeMacros.expandir(linhasClassificadas);
        for(String linha: codigoFinal){
            System.out.println(linha);
        }

        EscritorDeArquivo escritor = new EscritorDeArquivo();
        escritor.escreverArquivo("MASMAPRG.txt", codigoFinal);

        // Macros antes das passagens
        PrimeiraPassagem pp = new PrimeiraPassagem();
        SegundaPassagem sp = new SegundaPassagem();
        TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
        TabelaInstrucao tabelaInstrucao = new TabelaInstrucao();

        pp.primeirapassagem(arquivoEntrada2, tabelaSimbolos, tabelaInstrucao);
        sp.segundapassagem(arquivoEntrada2, arquivoSaida, tabelaSimbolos, tabelaInstrucao);
        controller.atualizarTabelaSimbolos(tabelaSimbolos);
    }

    public static void main(String[] args) {
        launch();
    }
}