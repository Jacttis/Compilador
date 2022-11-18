package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Collections;
import java.util.LinkedList;

public class NodoAccesoMetodoEstatico extends NodoAcceso {
    Token tokenClase;
    LinkedList<NodoExpresion> parametros;

    protected Clase claseActual;
    protected MetodoConstructor metodoActual;

    protected Metodo metodoLLamado;
    public NodoAccesoMetodoEstatico(Token token,Token tokenClase) {
        super(token);
        this.tokenClase=tokenClase;
        esLlamable=true;
    }

    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }


    public Tipo chequear() {
        Clase clase = TablaDeSimbolos.tablaSimbolos.getClaseByName(tokenClase.getLexeme());
        if (clase !=null){
            claseActual=clase;
            Metodo met = clase.tieneMetodoExacto(accesoToken.getLexeme(), parametros, true);
            if (met != null) {
                metodoLLamado=met;
                if (nodoEncadenado != null) {
                    Tipo tipoEncadenado = nodoEncadenado.chequear(met.getTipo());
                    if (tipoEncadenado != null) {
                        return tipoEncadenado;
                    } else {
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                                + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                        return new Tipo(new Token("pr_void", "void", 0));//Devuelvo para seguir ejecucion
                    }
                } else {
                    return met.getTipo();
                }
            } else {
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": No existe un metodo con ese nombre o incorrectos parametros o no es estatico " + accesoToken.getLexeme()));

                return new Tipo(new Token("pr_void", "void", 0)); //Devuelvo esto para que no termine la ejecucion
            }
        }
        else{
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenClase, "Error Semantico en linea "
                    + tokenClase.getNumberline() + ": No existe una Clase " + tokenClase.getLexeme()));

            return new Tipo(new Token("pr_void", "void", 0)); //Devuelvo esto para que no termine la ejecucion
        }
    }

    @Override
    public void generarCodigo() {
            if (!metodoLLamado.getTipo().compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.codigoMaquina.add("RMEM 1");
            }
            Collections.reverse(parametros);
            for (NodoExpresion expresion:parametros) {
                expresion.generarCodigo();
            }
            Collections.reverse(parametros);
            TablaDeSimbolos.codigoMaquina.add("PUSH "+metodoLLamado.generarEtiqueta());
            TablaDeSimbolos.codigoMaquina.add("CALL");

        if(nodoEncadenado != null){
            if (ladoIzquierdo)
                nodoEncadenado.setLadoIzquierdo();
            nodoEncadenado.generarCodigo();
        }
    }
}
