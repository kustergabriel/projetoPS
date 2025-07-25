package com.app.calingaertextend.processadordemacros;

import java.util.*;

public class TabelaDeMacros {
    private Map<String, Macro> macros = new HashMap<>();

    public void processarMacros(List<ListaAsm> linhas) {
        Stack<MacroBuilder> construtores = new Stack<>();

        for (ListaAsm linha : linhas) {
            String tipo = linha.getTipo();
            String conteudo = linha.getConteudo();
            String[] tokens = conteudo.trim().toUpperCase().split("\\s+");

            switch (tipo) {
                case "inicio_macro":
                    construtores.push(new MacroBuilder());
                    break;
                
                case "cabecalho_macro":
                    if (!construtores.isEmpty()) {
                        construtores.peek().setCabecalho(tokens, conteudo);
                    }
                    break;

                case "codigo_macro":
                    if (!construtores.isEmpty()) {
                        construtores.peek().addLinhaCorpo(conteudo);
                    }
                    break;

                case "fim_macro":
                    if (!construtores.isEmpty()) {

                        MacroBuilder builderConcluido = construtores.pop();
                        Macro macroConcluida = builderConcluido.build();
                        

                        if (macroConcluida.getNome() != null && !macroConcluida.getNome().isEmpty()) {
                            this.macros.put(macroConcluida.getNome(), macroConcluida);
                        }

                        if (!construtores.isEmpty()) {
                            construtores.peek().addLinhaCorpo("MACRO");
                            construtores.peek().addLinhaCorpo(builderConcluido.getCabecalhoOriginal());
                            for(String linhaCorpoFilha : macroConcluida.getCorpo()){
                                construtores.peek().addLinhaCorpo(linhaCorpoFilha);
                            }
                            construtores.peek().addLinhaCorpo("MEND");
                        }
                    }
                    break;
            }
        }
    }

    public void listarMacros() {
        if (macros.isEmpty()) {
            System.out.println("Nenhuma macro foi definida.");
            return;
        }
        for (Macro macro : macros.values()) {
            System.out.println("Macro: " + macro.getNome());
            System.out.println("  Parâmetros: " + macro.getParametros());
            System.out.println("  Corpo:");
            for (String linhaCorpo : macro.getCorpo()) {
                System.out.println("    " + linhaCorpo);
            }
            System.out.println("------------------------------------");
        }
    }

    public Map<String, Macro> getMacros() {
        return macros;
    }

public class MacroBuilder {
    private String nome;
    private List<String> parametros = new ArrayList<>();
    private List<String> corpo = new ArrayList<>();
    private String cabecalhoOriginal;
    private boolean cabecalhoDefinido = false;

void setCabecalho(String[] tokens, String conteudoOriginal) {
    this.cabecalhoOriginal = conteudoOriginal;
    this.parametros.clear(); // Limpa a lista para garantir

    List<String> tokensComoLista = new ArrayList<>(Arrays.asList(tokens));
    
    // Identifica o nome e remove da lista de tokens, sobrando apenas os parâmetros
    if (tokens.length > 1 && tokens[0].startsWith("&")) {
        this.nome = tokens[1];
        tokensComoLista.remove(1); // Remove o nome
    } else {
        this.nome = tokens[0];
        tokensComoLista.remove(0); // Remove o nome
    }

    // Itera nos tokens que sobraram (que são os parâmetros) e os limpa
    for (String tokenParametro : tokensComoLista) {
        String parametroLimpo = tokenParametro.replace(",", "");
        this.parametros.add(parametroLimpo);
    }
    
    this.cabecalhoDefinido = true;
}


    void addLinhaCorpo(String linha) { this.corpo.add(linha); }
    boolean hasCabecalho() { return this.cabecalhoDefinido; }
    String getCabecalhoOriginal() { return this.cabecalhoOriginal; }

    Macro build() {
        return new Macro(nome, new ArrayList<>(parametros), new ArrayList<>(corpo));
    }
}

}