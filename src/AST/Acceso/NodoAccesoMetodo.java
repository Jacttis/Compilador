package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAcceso{


    protected LinkedList<NodoExpresion> parametros;

    public NodoAccesoMetodo(Token token) {
        super(token);
    }

    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> expresiones) {
        this.parametros = expresiones;
    }
}
