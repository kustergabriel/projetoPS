package com.app.calingaertextend;

import com.app.calingaertextend.UI.LinhaMemoria;
import com.app.calingaertextend.UI.LinhaRegistrador;
import com.app.calingaertextend.UI.LinhaSimbolo;
import com.app.calingaertextend.montador.Simbolos;
import com.app.calingaertextend.montador.TabelaDeSimbolos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.app.calingaertextend.maquinavirtual.Registradores;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;

public class ViewController {

    @FXML
    public TextArea codigoFonte;
    @FXML
    private TextArea codigoExpandido;
    @FXML
    private TextArea codigoMontado;
    @FXML
    private TableView<LinhaMemoria> tabelaMemoria;
    @FXML
    private TableColumn<LinhaMemoria, String> colunaEndereco;
    @FXML
    private TableColumn<LinhaMemoria, Integer> colunaValor;
    private final ObservableList<LinhaMemoria> dadosMemoria = FXCollections.observableArrayList();
    @FXML
    private TableView<LinhaRegistrador> tabelaDeRegistradores;
    @FXML
    private TableColumn<LinhaRegistrador, String> colunaRegistrador;
    @FXML
    private TableColumn<LinhaRegistrador, Integer> colunaValorRegistrador;
    private final ObservableList<LinhaRegistrador> dados = FXCollections.observableArrayList();
    @FXML
    private TableView<LinhaSimbolo> tabelaDeSimbolos;
    @FXML
    private TableColumn<LinhaSimbolo, String> colunaSimboloNome;
    @FXML
    private TableColumn<LinhaSimbolo, Integer> colunaSimboloEndereco;
    @FXML
    private TableColumn<LinhaSimbolo, String> colunaSimboloTipo;
    @FXML
    private TableColumn<LinhaSimbolo, String> colunaSimboloStatus;
    private final ObservableList<LinhaSimbolo> dadosSimbolos = FXCollections.observableArrayList();
    private Stage stage;
    private Main mainApp;

    @FXML
    public void initialize() {
        colunaRegistrador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaValorRegistrador.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tabelaDeRegistradores.setItems(dados);
        tabelaMemoria.setItems(dadosMemoria);
        tabelaDeSimbolos.setItems(dadosSimbolos);
        colunaSimboloNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaSimboloEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colunaSimboloTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaSimboloStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void atualizarTabelaMemoria(int[] vetorMemoria) {
        dadosMemoria.clear();
        for (int i = 0; i < vetorMemoria.length; i++) {
            String enderecoFormatado = String.format("%04d", i);
            dadosMemoria.add(new LinhaMemoria(enderecoFormatado, vetorMemoria[i]));
        }
    }

    public void atualizarTabela(Registradores registradores){
        dados.setAll(
                new LinhaRegistrador("PC", registradores.getPC()),
                new LinhaRegistrador("SP", registradores.getSP()),
                new LinhaRegistrador("ACC", registradores.getACC()),
                new LinhaRegistrador("MOP", registradores.getMOP()),
                new LinhaRegistrador("RI", registradores.getRI()),
                new LinhaRegistrador("RE", registradores.getRE()),
                new LinhaRegistrador("R0", registradores.getR0()),
                new LinhaRegistrador("R1", registradores.getR1())
        );
    }

    public void atualizarTabelaSimbolos(TabelaDeSimbolos tabelaDeSimbolos){
        dadosSimbolos.clear();
        for(Simbolos simbolo: tabelaDeSimbolos.getTodosSimbolos()){
            LinhaSimbolo linha = new LinhaSimbolo(simbolo.getEndereco(), simbolo.getRotulo(), simbolo.getTipo(), simbolo.getStatus());
            dadosSimbolos.add(linha);
        }
    }

    public void atualizarCodigoExpandido(String codigo){
        codigoExpandido.setText(codigo);
    }

    public void atualizarCodigoMontado(String codigo){
        codigoMontado.setText(codigo);
    }

    @FXML
    public void onReiniciarClick(){
        codigoFonte.setText("");
        codigoExpandido.setText("");
        codigoMontado.setText("");

        // Limpa os dados das três tabelas
        dados.clear();
        dadosMemoria.clear();
        dadosSimbolos.clear();

    }

    @FXML
    public void onAbrirArquivoClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
        );

        if (stage != null) {
            File arquivoSelecionado = fileChooser.showOpenDialog(stage);
            if (arquivoSelecionado != null) {
                try {
                    String codigo = Files.readString(arquivoSelecionado.toPath());
                    codigoFonte.setText(codigo);
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo: " + e.getMessage());
                }
            }
        } else {
            System.err.println("Stage não foi definido no controlador!");
        }
    }

    @FXML
    public void onMontarCodigoClick(){
        String codigoDeEntrada = codigoFonte.getText();
        if (mainApp != null && !codigoDeEntrada.isEmpty()) {
            mainApp.executarProcessoCompleto(codigoDeEntrada);
        }
    }

    @FXML
    public void onExecutarCodigoClick(){
        if (mainApp != null && !codigoMontado.getText().isEmpty()) {
            mainApp.carregarEExecutar();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
        // *** MÉTODO DE ERRO ADICIONADO ***
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}