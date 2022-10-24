package AST;

import AST.Expresion.NodoExpresion;

public class NodoReturn extends NodoSentencia{

    protected NodoExpresion expresion;

    public NodoReturn(NodoExpresion expresion){
        this.expresion=expresion;
    }
}
