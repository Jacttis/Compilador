package AST;

import AST.Expresion.NodoExpresion;

public class NodoIf extends NodoSentencia{
    protected NodoExpresion expresion;
    protected NodoSentencia sentencia;
    protected NodoElse nodoElse;

    public NodoIf(NodoExpresion expresion,NodoSentencia sentencia){
        this.expresion=expresion;
        this.sentencia=sentencia;
    }

    public NodoElse getNodoElse() {
        return nodoElse;
    }

    public void setNodoElse(NodoElse nodoElse) {
        this.nodoElse = nodoElse;
    }

    @Override
    public void chequear() {
        expresion
    }
}
