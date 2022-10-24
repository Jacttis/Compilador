package AST;

import AST.Expresion.NodoExpresion;

public class NodoWhile extends NodoSentencia{
    protected NodoExpresion expresion;
    protected NodoSentencia sentencia;

    public NodoWhile(NodoExpresion expresion,NodoSentencia sentencia){
        this.expresion=expresion;
        this.sentencia=sentencia;
    }
}
