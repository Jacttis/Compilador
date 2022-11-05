package AnalizadorSemantico;

import AST.Acceso.NodoAcceso;
import AST.NodoSentencia;
import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

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
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme()));
        }

    }
    public boolean checkTipo(IClaseInterfaz claseActual){
        boolean ret=true;
        if(TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(tokenTipo.getLexeme()) || TablaDeSimbolos.tablaSimbolos.getClases().containsKey(tokenTipo.getLexeme()) || claseActual.getParametrosGenericos().containsKey(tokenTipo.getLexeme())){
            for (Token token:parametrosGenericos.values()) {
                if (!TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(token.getLexeme()) && !TablaDeSimbolos.tablaSimbolos.getClases().containsKey(token.getLexeme()) && !claseActual.getParametrosGenericos().containsKey(token.getLexeme())){
                    ret=false;
                }
            }
        }
        else {
            ret=false;
        }
        return ret;
    }
    public boolean esSubtipo(Tipo tipo, NodoSentencia accesoLLama){
        if(this.compareTipo(new TipoPrimitivo(new Token("pr_null","null",0)))&& tipo instanceof TipoReferencia){
            return true;
        }
        if(TablaDeSimbolos.tablaSimbolos.getClases().containsKey(tipo.getToken().getLexeme())){
            return TablaDeSimbolos.tablaSimbolos.getClaseByName(tipo.getToken().getLexeme()).esSubtipo(tokenTipo.getLexeme());
        } else if (TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(tipo.getToken().getLexeme())) {
            Clase clase=TablaDeSimbolos.tablaSimbolos.getClaseByName(tipo.getToken().getLexeme());
            if(clase!=null){
                return clase.esSubtipo(tokenTipo.getLexeme());
            }
            else{
                Interfaz interfaz=TablaDeSimbolos.tablaSimbolos.getInterfazByName(tipo.getToken().getLexeme());
                if(interfaz!=null){
                    return interfaz.esSubtipo(tokenTipo.getLexeme());
                }
                else{
                    return false;
                }
            }
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(accesoLLama.getToken(), "Error Semantico en linea "
                    + accesoLLama.getToken().getNumberline() + ": Argumento mas definido la clase no existe o es tipo Primitivo " + accesoLLama.getToken().getLexeme()));
            return false;
        }
    }
    public boolean esSubtipo(Tipo tipo){
        if(this.compareTipo(new TipoPrimitivo(new Token("pr_null","null",0)))&& tipo instanceof TipoReferencia){
            return true;
        }
        if(TablaDeSimbolos.tablaSimbolos.getClases().containsKey(tipo.getToken().getLexeme())){
            return TablaDeSimbolos.tablaSimbolos.getClaseByName(tipo.getToken().getLexeme()).esSubtipo(tokenTipo.getLexeme());
        } else if (TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(tipo.getToken().getLexeme())) {
                Interfaz interfaz=TablaDeSimbolos.tablaSimbolos.getInterfazByName(tipo.getToken().getLexeme());
                if(interfaz!=null){
                    return interfaz.esSubtipo(tokenTipo.getLexeme());
                }
                else{
                    return false;
                }
            }
        return false;

    }


}
