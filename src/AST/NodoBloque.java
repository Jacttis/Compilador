package AST;

import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.MetodoConstructor;

import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque {

    protected LinkedList<NodoSentencia> listaSentencias;
    protected Hashtable<String, NodoVarLocal> varLocales;
    protected MetodoConstructor metodoBloque;
    protected NodoBloque bloqueContainer;


    public NodoBloque(MetodoConstructor metodoConstructor){
        metodoBloque=metodoConstructor;
        listaSentencias=new LinkedList<>();
        varLocales=new Hashtable<>();
    }


}
