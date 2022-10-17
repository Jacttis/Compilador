package AST;

import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.MetodoConstructor;

import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque {

    protected LinkedList<NodoSentencia> listaSentencias;
    protected Hashtable<String, NodoVarLocal> varLocales;
    protected MetodoConstructor metodoBloque;

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

    protected NodoBloque bloqueContainer;


    public NodoBloque(MetodoConstructor metodoConstructor){
        metodoBloque=metodoConstructor;
        listaSentencias=new LinkedList<>();
        varLocales=new Hashtable<>();
    }


}
