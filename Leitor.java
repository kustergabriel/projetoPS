import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


// é a classe que vai de fato ler o arquivo e separar linha por linha, tokenizar e indentificar o tipo e 
//o conteudo da linha pra usar a outra classe ListaAsm

public class Leitor {
    private List<ListaAsm> linhasFeitas = new ArrayList();
    private Set<String> macrosDefinidas = new HashSet <>();

    public void lerArquivo (String caminho){ 

        System.out.println("\nProcessando: " + caminho);
       try (Scanner scanner = new Scanner(new File(caminho))){
        boolean dentroMacro = false;
        String nomeMacroAtual = "";
        boolean aguardandoCabecalho = false;
        

            while (scanner.hasNextLine()){
                String linhaOriginal = scanner.nextLine().trim();
                String primeiraPalavra;

             if (linhaOriginal == null || linhaOriginal.isEmpty()){
                continue; //aqui ve se a linha ta vazia ou se é comentário
                }
            if (linhaOriginal.startsWith("*")){
                continue;
            }
            
        
            //tokeniza
            String[] tokens = linhaOriginal.split("\\s+");
            List <String> tokensLimpos = new ArrayList<>();

            for (String token : tokens){
                if (!token.isEmpty() && !token.startsWith("*")){
                    tokensLimpos.add(token.toUpperCase());
                } 
            }

            if (tokensLimpos.isEmpty()) continue;
  
            String tipo = "";
            primeiraPalavra = tokensLimpos.get(0); //aqui é pra pegar o primeiro token da linha

            if (primeiraPalavra.equalsIgnoreCase("MACRO")) {
                tipo = "inicio_macro";
                dentroMacro = true;
                aguardandoCabecalho = true; 
            
            } else if (primeiraPalavra.equalsIgnoreCase("MEND")){
                tipo = "fim_macro";
                dentroMacro = false;
                nomeMacroAtual = "";

            } else if (dentroMacro && aguardandoCabecalho){
                if (tokensLimpos.isEmpty()){
                    System.err.println("Erro: Macro sem cabeçalho");
                }
                tipo = "cabecalho_macro";
                nomeMacroAtual = primeiraPalavra; //aq salva o nome da MACRO
                macrosDefinidas.add(nomeMacroAtual);
                aguardandoCabecalho = false;

            } else if (dentroMacro){
                tipo = "codigo_macro";

            } else if (macrosDefinidas.contains(primeiraPalavra)){
                tipo = "chamada_macro";

            } else if (primeiraPalavra.endsWith(":")){
                tipo = "label";

            } else if (aguardandoCabecalho && tokensLimpos.isEmpty()) {
    System.err.println("Erro: Macro sem cabeçalho");

            } else {
                tipo = "codigo";
            } 
            linhasFeitas.add(new ListaAsm(linhaOriginal,tipo));
          }
          
        } catch (FileNotFoundException e) {
        System.out.println("Arquivo não reconhecido" + caminho);
       }

    System.out.println("<<<<Linhas classificadas>>>>");
    for (ListaAsm linha : linhasFeitas) {
    System.out.println(linha.getTipo() + "" + linha.getConteudo());
    System.out.println("Total de linhas: " + linhasFeitas.size());

    }

    }

}