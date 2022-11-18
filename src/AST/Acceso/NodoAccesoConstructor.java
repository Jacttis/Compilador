package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Collections;
import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAcceso {

    protected LinkedList<NodoExpresion> parametros;
    Clase claseConstructor;

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
            claseConstructor=clase;
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

    public void generarCodigo(){
        TablaDeSimbolos.codigoMaquina.add("RMEM 1");
        TablaDeSimbolos.codigoMaquina.add("PUSH "+(claseConstructor.getAtributos().size()+1));
        TablaDeSimbolos.codigoMaquina.add("PUSH lmalloc");
        TablaDeSimbolos.codigoMaquina.add("CALL");
        TablaDeSimbolos.codigoMaquina.add("DUP");
        TablaDeSimbolos.codigoMaquina.add("PUSH VT_"+accesoToken.getLexeme());
        TablaDeSimbolos.codigoMaquina.add("STOREREF 0");
        Collections.reverse(parametros);
        for (NodoExpresion expresion: parametros) {
            expresion.generarCodigo();
            TablaDeSimbolos.codigoMaquina.add("SWAP");
        }
        Collections.reverse(parametros);
        TablaDeSimbolos.codigoMaquina.add("PUSH "+claseConstructor.retornarConstructor(parametros).generarEtiqueta());
        TablaDeSimbolos.codigoMaquina.add("CALL");

        if(nodoEncadenado != null){
            if (ladoIzquierdo)
                nodoEncadenado.setLadoIzquierdo();
            nodoEncadenado.generarCodigo();

        }
    }


}
