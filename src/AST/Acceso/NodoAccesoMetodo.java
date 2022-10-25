package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAcceso{


    protected LinkedList<NodoExpresion> parametros;

    public NodoAccesoMetodo(Token token) {
        super(token);
        esLlamable=true;
    }

    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> expresiones) {
        this.parametros = expresiones;
    }


    @Override
    public Tipo chequear() {
        return super.chequear();
    }
}
