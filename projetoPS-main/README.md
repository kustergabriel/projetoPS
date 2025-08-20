# Projeto de um Sistema de Programação para um Computador Hipotético

#### Grupo composto por: Felipe Veloso, Gabriel Kuster de Azevedo, Junior Prediger, Luana Montilha, Luiz Kolosque, Matheus Zehetmeyr, Rodrigo Iasculaski.

## Como compilar: 
#### Instale o JavaFX: https://gluonhq.com/products/javafx/
#### Extraia para o diretorio de sua preferencia!

#### Navegue até o diretorio da pasta Calingaert-Extend
##### javac --module-path "DIRETORIO DA PASTA JAVAFX" --add-modules javafx.controls,javafx.fxml -d out src\main\java\com\app\calingaertextend\*.java
##### xcopy src\main\resources out /E /I /Y
##### java --module-path "DIRETORIO DA PASTA JAVAFX" --add-modules javafx.controls,javafx.fxml -cp out com.app.calingaertextend.Main
