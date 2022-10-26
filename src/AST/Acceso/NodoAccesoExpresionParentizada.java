package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
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
            Tipo tipoEncadenado= nodoEncadenado.chequear(tipo); //ver
            if(tipoEncadenado!=null){
                return tipoEncadenado;
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
            }
        }
    }
}
