package com.app.calingaertextend;

import com.app.calingaertextend.UI.LinhaMemoria;
import com.app.calingaertextend.UI.LinhaRegistrador;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.stream.Collectors;

public class ViewController {

    @FXML
    private Button buttonAction;

    @FXML
    private TitledPane memoria;

    @FXML
    private TableView<LinhaMemoria> tabelaMemoria;

    @FXML
    private TableColumn<LinhaMemoria, String> colunaEndereco;

    @FXML
    private TableColumn<LinhaMemoria, Integer> colunaValor;

    private final ObservableList<LinhaMemoria> dadosMemoria = FXCollections.observableArrayList();

    public void atualizarTabelaMemoria(int[] vetorMemoria) {
        dadosMemoria.clear();
        for (int i = 0; i < vetorMemoria.length; i++) {
            String enderecoFormatado = String.format("%04d", i);
            dadosMemoria.add(new LinhaMemoria(enderecoFormatado, vetorMemoria[i]));
        }
    }

    @FXML
    private TitledPane registrador;

    @FXML
    private TableView<LinhaRegistrador> tabelaDeRegistradores;

    @FXML
    private TableColumn<LinhaRegistrador, String> colunaRegistrador;

    @FXML
    private TableColumn<LinhaRegistrador, Integer> colunaValorRegistrador;

    private final ObservableList<LinhaRegistrador> dados = FXCollections.observableArrayList();

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
    @FXML
    public void initialize() {
        colunaRegistrador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaValorRegistrador.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tabelaDeRegistradores.setItems(dados);
        tabelaMemoria.setItems(dadosMemoria);
    }


    @FXML
    private TextArea codigo;

    @FXML
    private Button buttonAtualizarRegistradores;

    @FXML
    public void passo(){

    }

    @FXML
    public void onBtClick(){
        try {
            InputStream inputStream = getClass().getResourceAsStream("/arquivos/exemplo.txt");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String conteudo = reader.lines().collect(Collectors.joining("\n"));
                codigo.setText(conteudo);
            } else {
                codigo.setText("Arquivo não encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            codigo.setText("Erro ao carregar o arquivo.");
        }
    }
}

