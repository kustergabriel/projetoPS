package com.app.calingaertextend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import com.app.calingaertextend.montador.PrimeiraPassagem;
import com.app.calingaertextend.montador.SegundaPassagem;
import com.app.calingaertextend.montador.TabelaDeSimbolos;
import com.app.calingaertextend.montador.TabelaInstrucao;

public class Main extends Application {

    public static Memoria memoria;
    public static Registradores registradores;
    public static Pilha pilha;
    public static Executor executor;
    public static ViewController controller;
    public static Leitor leitor;
    public static TabelaDeMacros tabela;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Calingaert Extend");
        stage.show();

        // Controller da Interface
        controller = fxmlLoader.getController();

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

        String arquivoEntrada = "C:/Users/Gabriel Azevedo/Documents/GitHub/projetoPS/Calingaert-Extend/src/main/java/com/app/calingaertextend/montador/teste.txt";
        String arquivoSaida = "java/saida.txt";   

        leitor = new Leitor();
        leitor.lerArquivo(arquivoEntrada);

        List<ListaAsm> linhasClassificadas = leitor.getLinhasFeitas();

        tabela = new TabelaDeMacros();
        tabela.processarMacros(linhasClassificadas);

        PrimeiraPassagem pp = new PrimeiraPassagem();
        SegundaPassagem sp = new SegundaPassagem();
        TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
        TabelaInstrucao tabelaInstrucao = new TabelaInstrucao();

        pp.primeirapassagem(arquivoEntrada, tabelaSimbolos, tabelaInstrucao);

        sp.segundapassagem(arquivoEntrada, arquivoSaida, tabelaSimbolos, tabelaInstrucao);
        
    }

    public static void main(String[] args) {
        launch();
    }
}