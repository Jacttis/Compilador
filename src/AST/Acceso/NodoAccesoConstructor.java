package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAcceso {

    protected LinkedList<NodoExpresion> parametros;

    public NodoAccesoConstructor(Token token) {
        super(token);
    }



    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    public Tipo chequear(){
        TablaDeSimbolos.tablaSimbolos.getClaseByName(accesoToken.getLexeme()).chequearContieneConstructor(parametros);
        if(nodoEncadenado!=null){
            return nodoEncadenado.chequear(new TipoReferencia(accesoToken));
        }
        return new TipoReferencia(accesoToken);
    }
}
