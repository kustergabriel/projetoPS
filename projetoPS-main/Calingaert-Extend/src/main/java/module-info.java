module com.app.calingaertextend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.app.calingaertextend to javafx.fxml; // Adicione esta linha

    // *** LINHA ADICIONADA PARA A CORREÇÃO ***
    // Abre o pacote da UI para que o JavaFX possa acessar as classes das tabelas
    opens com.app.calingaertextend.UI to javafx.base;

    exports com.app.calingaertextend;
    exports com.app.calingaertextend.processadordemacros; // Exporta o pacote do Leitor
    exports com.app.calingaertextend.maquinavirtual; // Exporta outros pacotes necessários
    exports com.app.calingaertextend.montador; // Exporta o pacote do montador
}