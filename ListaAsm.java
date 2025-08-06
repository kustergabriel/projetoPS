public class ListaAsm{
   private String conteudo;
   private String tipo;

    ListaAsm (String conteudo, String tipo){
        this.conteudo = conteudo;
        this.tipo = tipo;
    }
    public String getConteudo(){
        return conteudo;
    }
    public String getTipo (){
        return tipo;
    }
}