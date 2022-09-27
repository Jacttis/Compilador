package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;

public class TipoReferencia extends Tipo{
    public Hashtable<String, Token> getParametrosGenericos() {
        return parametrosGenericos;
    }

    Hashtable<String, Token> parametrosGenericos;
    public TipoReferencia(Token actualToken) {
        super(actualToken);
        parametrosGenericos=new Hashtable<>();
    }

    public void agregarParametro(Token parametro) throws SemanticException {
        if(!parametrosGenericos.containsKey(parametro.getLexeme())){
            parametrosGenericos.put(parametro.getLexeme(),parametro);
        }
        else{
            throw new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme());
        }

    }
    public boolean checkTipo(Clase claseActual){
        boolean ret=true;
        if(TablaDeSimbolos.tablaSimbolos.getClases().containsKey(tokenTipo.getLexeme()) && claseActual.parametrosGenericos.containsKey(tokenTipo.getLexeme())){
            for (Token token:parametrosGenericos.values()) {
                if (!TablaDeSimbolos.tablaSimbolos.getClases().containsKey(token.getLexeme()) && !claseActual.parametrosGenericos.containsKey(token.getLexeme())){
                    ret=false;
                }
            }
        }
        else {
            ret=false;
        }
        return ret;
    }
}
