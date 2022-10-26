package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAcceso {

    protected LinkedList<NodoExpresion> parametros;

    public NodoAccesoConstructor(Token token) {
        super(token);
        esLlamable=true;
    }



    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    public Tipo chequear(){
        Clase clase=TablaDeSimbolos.tablaSimbolos.getClaseByName(accesoToken.getLexeme());
        if(clase!=null){
            clase.chequearContieneConstructor(parametros);
            if(nodoEncadenado!=null){
                Tipo tipo= nodoEncadenado.chequear(new TipoReferencia(accesoToken));
                if(tipo!=null){
                    return tipo;
                }
                else{
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                            + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                    return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
                }
            }
            return new TipoReferencia(accesoToken);
        }
        else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                    + accesoToken.getNumberline() + ": No existe una Clase " + accesoToken.getLexeme()));

            return new Tipo(new Token("pr_void", "void", 0)); //Devuelvo esto para que no termine la ejecucion
        }
    }


}
