package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

public class NodoAccesoExpresionParentizada extends NodoAcceso {
    NodoExpresion nodoExpresion;
    public NodoAccesoExpresionParentizada(NodoExpresion nodoExpresion) {
        super(null);
        this.nodoExpresion=nodoExpresion;
    }

    @Override
    public Tipo chequear() {
        Tipo tipo=nodoExpresion.chequear();
        if(nodoEncadenado==null) {
            return tipo;
        }
        else {
            return nodoEncadenado.chequear((TipoReferencia) tipo); //ver
        }
    }
}
