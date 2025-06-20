package com.app.calingaertextend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Memoria memoria;
    public static Registradores registradores;
    public static Pilha pilha;
    public static Executor executor;
    public static ViewController controller;

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

        executor.carregarPrograma(); // carrega o código na memória

        try {
            executor.executarPasso();
        } catch (AcessoIndevidoAMemoriaCheckedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}