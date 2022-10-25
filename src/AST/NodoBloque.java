package AST;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    protected LinkedList<NodoSentencia> listaSentencias;
    protected Hashtable<String, NodoVarLocal> varLocales;
    protected MetodoConstructor metodoBloque;
    protected NodoBloque bloqueContainer;


    public NodoBloque(MetodoConstructor metodoConstructor){
        metodoBloque=metodoConstructor;
        listaSentencias=new LinkedList<>();
        varLocales=new Hashtable<>();
    }
    public void addSentencia(NodoSentencia sentencia){
        listaSentencias.add(sentencia);
    }

    public void addVarLocal(NodoVarLocal varLocal) throws SemanticException {
        if(!varLocales.containsKey(varLocal.getTokenVar().getLexeme())){
            varLocales.put(varLocal.getTokenVar().getLexeme(),varLocal);
        }
        else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(varLocal.getTokenVar(), "Error Semantico en linea "
                    + varLocal.getTokenVar().getNumberline() + ": Ya hay un argumento declarado con el nombre " + varLocal.getTokenVar().getLexeme()));
        }
    }

    public LinkedList<NodoSentencia> getListaSentencias() {
        return listaSentencias;
    }

    public void setListaSentencias(LinkedList<NodoSentencia> listaSentencias) {
        this.listaSentencias = listaSentencias;
    }

    public Hashtable<String, NodoVarLocal> getVarLocales() {
        return varLocales;
    }

    public void setVarLocales(Hashtable<String, NodoVarLocal> varLocales) {
        this.varLocales = varLocales;
    }

    public MetodoConstructor getMetodoBloque() {
        return metodoBloque;
    }

    public void setMetodoBloque(MetodoConstructor metodoBloque) {
        this.metodoBloque = metodoBloque;
    }

    public NodoBloque getBloqueContainer() {
        return bloqueContainer;
    }

    public void setBloqueContainer(NodoBloque bloqueContainer) {
        this.bloqueContainer = bloqueContainer;
    }


    public void chequearBloque(Clase claseActual) {
        for (NodoSentencia sentencia:listaSentencias) {
            sentencia.chequear();
        }
        for (NodoVarLocal varLocal: varLocales.values()) {
            varLocal.chequear(claseActual);
        }
    }

    public boolean estaVarEnBloque(Token token){
        if(bloqueContainer!=null){
            return varLocales.containsKey(token.getLexeme()) || bloqueContainer.estaVarEnBloque(token);
        }
        else{
            return varLocales.containsKey(token.getLexeme());
        }

    }

    public Tipo tipoVarEnBloque(Token token){
        if(bloqueContainer!=null){
            if(varLocales.containsKey(token.getLexeme())) {
                return varLocales.get(token.getLexeme()).tipo;
            }
             return bloqueContainer.tipoVarEnBloque(token);
        }
        else{
            if(varLocales.containsKey(token.getLexeme())) {
                return varLocales.get(token.getLexeme()).tipo;
            }
            else {
                return null;
            }
        }

    }
}
