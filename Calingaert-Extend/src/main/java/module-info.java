module com.app.calingaertextend {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.app.calingaertextend to javafx.fxml; // Adicione esta linha
    exports com.app.calingaertextend;
    exports com.app.calingaertextend.processadordemacros; // Exporta o pacote do Leitor
    exports com.app.calingaertextend.maquinavirtual; // Exporta outros pacotes necessários
    exports com.app.calingaertextend.montador; // Exporta o pacote do montador
}