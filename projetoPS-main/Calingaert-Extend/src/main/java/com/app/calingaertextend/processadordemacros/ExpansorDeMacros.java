package com.app.calingaertextend.processadordemacros;

import java.util.*;

public class ExpansorDeMacros {

    private final TabelaDeMacros tabelaDeMacros;

    public ExpansorDeMacros(TabelaDeMacros tabelaDeMacros) {
        this.tabelaDeMacros = tabelaDeMacros;
    }

    public List<String> expandir(List<ListaAsm> linhasClassificadas) {
        List<String> codigoFinal = new ArrayList<>();
        Deque<String> fila = new LinkedList<>();

        for (ListaAsm linha : linhasClassificadas) {
            if (linha.getTipo().equals("codigo") || linha.getTipo().equals("chamada_macro")) {
                fila.addLast(linha.getConteudo());
            }
        }

        while (!fila.isEmpty()) {
            String linhaAtual = fila.removeFirst();
            String[] tokens = linhaAtual.trim().split("[\\s,]+");

            String nomeDaMacro = null;
            if (tabelaDeMacros.getMacros().containsKey(tokens[0].toUpperCase())) {
                nomeDaMacro = tokens[0].toUpperCase();
            } else if (tokens.length > 1 && tabelaDeMacros.getMacros().containsKey(tokens[1].toUpperCase())) {
                nomeDaMacro = tokens[1].toUpperCase();
            }

            if (nomeDaMacro != null) {
                Macro macro = tabelaDeMacros.getMacros().get(nomeDaMacro);
                if (isDefinicaoDeMacro(macro)) {
                    expandirDefinicao(tokens, macro);
                } else {
                    List<String> blocoExpandido = expandirCodigo(tokens, macro);
                    for (int i = blocoExpandido.size() - 1; i >= 0; i--) {
                        fila.addFirst(blocoExpandido.get(i));
                    }
                }
            } else {
                codigoFinal.add(linhaAtual);
            }
        }
        return codigoFinal;
    }

    private List<String> expandirCodigo(String[] tokensDaChamada, Macro macro) {
        Map<String, String> mapaDeArgumentos = new HashMap<>();
        List<String> parametros = macro.getParametros();
        List<String> argumentos = new ArrayList<>(Arrays.asList(tokensDaChamada));

        boolean temRotulo = !argumentos.get(0).equalsIgnoreCase(macro.getNome());

        if (temRotulo) {
            if (!parametros.isEmpty()) {
                mapaDeArgumentos.put(parametros.get(0), argumentos.get(0));
            }
            argumentos.remove(0);
        } else {
            if (!parametros.isEmpty() && parametros.get(0).startsWith("&") && parametros.size() > (argumentos.size() - 1)) {
                mapaDeArgumentos.put(parametros.get(0), "");
            }
        }
        
        if (!argumentos.isEmpty()) {
            argumentos.remove(0);
        }

        for (String parametro : parametros) {
            if (mapaDeArgumentos.containsKey(parametro)) continue;
            if (!argumentos.isEmpty()) {
                mapaDeArgumentos.put(parametro, argumentos.remove(0));
            }
        }


        List<String> corpoExpandido = new ArrayList<>();
        for (String linhaCorpo : macro.getCorpo()) {
            String linhaSubstituida = linhaCorpo;
            for (Map.Entry<String, String> entry : mapaDeArgumentos.entrySet()) {
                linhaSubstituida = linhaSubstituida.replace(entry.getKey(), entry.getValue());
            }
            corpoExpandido.add(linhaSubstituida);
        }
        return corpoExpandido;
    }

    private void expandirDefinicao(String[] tokensDaChamada, Macro macroMae) {
        Map<String, String> mapaDeArgumentosMae = new HashMap<>();
        for (int i = 0; i < macroMae.getParametros().size(); i++) {
            if ((i + 1) < tokensDaChamada.length) {
                mapaDeArgumentosMae.put(macroMae.getParametros().get(i), tokensDaChamada[i + 1]);
            }
        }

        List<String> corpoDaMacroAninhadaAtual = new ArrayList<>();
        boolean dentroDeAninhada = false;
        
        for (String linha : macroMae.getCorpo()) {
            String linhaTrim = linha.trim();
            String[] tokens = linhaTrim.split("\\s+");

            if (tokens[0].equalsIgnoreCase("MACRO")) {
                dentroDeAninhada = true;
                corpoDaMacroAninhadaAtual.clear();
                continue;
            }
            
            if (tokens[0].equalsIgnoreCase("MEND")) {
                dentroDeAninhada = false;
                
                String cabecalho = corpoDaMacroAninhadaAtual.remove(0).trim();
                String[] tokensCabecalho = cabecalho.split("[\\s,]+");
                
                String nomeAninhada = tokensCabecalho[0];
                List<String> paramsAninhada = new ArrayList<>();
                for (int i = 1; i < tokensCabecalho.length; i++) {
                    paramsAninhada.add(tokensCabecalho[i]);
                }

                List<String> corpoProcessado = new ArrayList<>();
                for (String linhaCorpoFilha : corpoDaMacroAninhadaAtual) {
                    String linhaSubstituida = linhaCorpoFilha;
                    for (Map.Entry<String, String> entrada : mapaDeArgumentosMae.entrySet()) {
                        linhaSubstituida = linhaSubstituida.replace(entrada.getKey(), entrada.getValue());
                    }
                    corpoProcessado.add(linhaSubstituida);
                }
                
                this.tabelaDeMacros.getMacros().put(nomeAninhada, new Macro(nomeAninhada, paramsAninhada, corpoProcessado));
                continue;
            } 
            
            if (dentroDeAninhada) {
                corpoDaMacroAninhadaAtual.add(linha);
            }
        }
    }
    
    private boolean isDefinicaoDeMacro(Macro macro) {
        for (String linha : macro.getCorpo()) {
            if (linha.trim().toUpperCase().startsWith("MACRO")) {
                return true;
            }
        }
        return false;
    }
}