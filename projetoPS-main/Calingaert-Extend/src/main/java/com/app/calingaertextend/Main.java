package com.app.calingaertextend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.app.calingaertextend.montador.*;
import com.app.calingaertextend.processadordemacros.*;
import com.app.calingaertextend.maquinavirtual.*;

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

        controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setMainApp(this);

        pilha = new Pilha(12);
    }

    public void executarProcessoCompleto(String codigoFonte) {
        try {
            String arquivoFonteOriginal = "codigo_fonte.txt";
            String arquivoExpandido = "MASMAPRG.txt";
            String arquivoObjetoMontado = "saida.obj";
            String arquivoExecutavel = "programa.hpx"; // O arquivo final e limpo

            Files.writeString(java.nio.file.Paths.get(arquivoFonteOriginal), codigoFonte);

            // 1. Processador de Macros
            Leitor leitor = new Leitor();
            leitor.lerArquivo(arquivoFonteOriginal);
            TabelaDeMacros tabelaDeMacros = new TabelaDeMacros();
            tabelaDeMacros.processarMacros(leitor.getLinhasFeitas());
            ExpansorDeMacros expansorDeMacros = new ExpansorDeMacros(tabelaDeMacros);
            List<String> codigoFinalExpandido = expansorDeMacros.expandir(leitor.getLinhasFeitas());
            new EscritorDeArquivo().escreverArquivo(arquivoExpandido, codigoFinalExpandido);
            Platform.runLater(() -> controller.atualizarCodigoExpandido(String.join("\n", codigoFinalExpandido)));

            // 2. Montador
            TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
            TabelaInstrucao tabelaInstrucao = new TabelaInstrucao();
            new PrimeiraPassagem().primeirapassagem(arquivoExpandido, tabelaSimbolos, tabelaInstrucao);
            new SegundaPassagem().segundapassagem(arquivoExpandido, arquivoObjetoMontado, tabelaSimbolos, tabelaInstrucao);
            Platform.runLater(() -> controller.atualizarTabelaSimbolos(tabelaSimbolos));

            // 3. Ligador
            List<File> modulosObjeto = Arrays.asList(new File(arquivoObjetoMontado));
            Map<String, Integer> tabelaGlobal = new LigadorPrimeiraPassagem().executarPassagem(modulosObjeto);
            new LigadorSegundaPassagem(tabelaGlobal).executarPassagem(modulosObjeto, arquivoExecutavel);

            // *** CORREÇÃO APLICADA AQUI ***
            // Exibe o conteúdo do arquivo final e limpo (programa.hpx)
            final String codigoFinalStr = Files.readString(java.nio.file.Paths.get(arquivoExecutavel));
            Platform.runLater(() -> controller.atualizarCodigoMontado(codigoFinalStr));

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> controller.showError("Erro de Processamento", "Ocorreu um erro durante a montagem: \n" + e.getMessage()));
        }
    }

    public void carregarEExecutar() {
        try {
            // Reinicia a VM
            memoria = new Memoria(12);
            registradores = new Registradores();
            executor = new Executor(memoria, registradores, pilha);
            executor.setController(controller);

            // *** CORREÇÃO APLICADA AQUI ***
            // Carrega o arquivo final e limpo (programa.hpx) para a memória
            String arquivoExecutavel = "programa.hpx";
            List<String> linhasDoExecutavel = Files.readAllLines(java.nio.file.Paths.get(arquivoExecutavel));

            int enderecoAtual = 0;
            for (String linha : linhasDoExecutavel) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.trim().split("\\s+");
                for (String parte : partes) {
                    memoria.setPosicaoMemoria(enderecoAtual++, Integer.parseInt(parte));
                }
            }
            
            registradores.setPC(0);

            // Atualiza a UI com o estado inicial
            Platform.runLater(() -> {
                controller.atualizarTabelaMemoria(memoria.getMemoria());
                controller.atualizarTabela(registradores);
            });

            // Executa em segundo plano
            new Thread(() -> {
                try {
                    executor.executarPasso();
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> controller.showError("Erro de Execução", "Aconteceu um erro na Máquina Virtual: \n" + e.getMessage()));
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> controller.showError("Erro de Carga", "Não foi possível carregar o programa para execução: \n" + e.getMessage()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}