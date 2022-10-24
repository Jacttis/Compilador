package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado {

    protected LinkedList<NodoExpresion> parametros;

    public NodoMetodoEncadenado(Token tokenNodoEncadenado) {
        super(tokenNodoEncadenado);
        parametros = new LinkedList<>();
    }


    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }
}
