package com.app.calingaertextend;
import java.util.*;

public class TabelaDeMacros {
    private Map<String, Macro> macros = new HashMap<>();

    public void processarMacros(List<ListaAsm> linhas) {
        boolean dentroMacro = false;
        String nomeMacro = null;
        List<String> parametros = new ArrayList<>();
        List<String> corpo = new ArrayList<>();

        for (int i = 0; i < linhas.size(); i++) {
            ListaAsm linha = linhas.get(i);
            String tipo = linha.getTipo();
            String conteudo = linha.getConteudo();

            switch (tipo) {
                case "cabecalho_macro":
                    String[] tokens = conteudo.trim().split("\\s+");
                    nomeMacro = tokens[0].toUpperCase();

                    parametros.clear();
                    for (int j = 1; j < tokens.length; j++) {
                        parametros.add(tokens[j]);
                    }

                    corpo.clear();
                    dentroMacro = true;
                    break;

                case "codigo_macro":
                    if (dentroMacro) {
                        corpo.add(conteudo);
                    }
                    break;

                case "fim_macro":
                    if (dentroMacro && nomeMacro != null) {
                        Macro macro = new Macro(nomeMacro, new ArrayList<>(parametros), new ArrayList<>(corpo));
                        macros.put(nomeMacro, macro);
                    }
                    dentroMacro = false;
                    nomeMacro = null;
                    break;
            }
        }
        listarMacros();
    }

    public void listarMacros() {
        System.out.println("\n<<<< Macros Definidas >>>>");
        for (Map.Entry<String, Macro> entry : macros.entrySet()) {
            Macro macro = entry.getValue();
            System.out.println("Macro: " + macro.getNome());
            System.out.println("Parâmetros: " + macro.getParametros());
            System.out.println("Corpo:");
            for (String linha : macro.getCorpo()) {
                System.out.println("   " + linha);
            }
            System.out.println("----------------------");
        }
    }

    public Map<String, Macro> getMacros() {
        return macros;
    }


    public void adicionarMacro(String nome, List<String> parametros, List<String> corpo) {
    if (nome == null || nome.isEmpty()) {
        throw new IllegalArgumentException("Nome da macro não pode ser vazio");
    }
    if (macros.containsKey(nome.toUpperCase())) {
        System.out.println("Aviso: Macro '" + nome + "' já existe e será substituída.");
    }
    Macro macro = new Macro(nome.toUpperCase(), parametros, corpo);
    macros.put(nome.toUpperCase(), macro);
    }


}
