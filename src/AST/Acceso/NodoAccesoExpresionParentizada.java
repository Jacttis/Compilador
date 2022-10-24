package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoAccesoExpresionParentizada extends NodoAcceso {
    NodoExpresion nodoExpresion;
    public NodoAccesoExpresionParentizada(NodoExpresion nodoExpresion) {
        super(null);
        this.nodoExpresion=nodoExpresion;
    }

    @Override
    public Tipo chequear() {
        return nodoExpresion.chequear();
    }
}
