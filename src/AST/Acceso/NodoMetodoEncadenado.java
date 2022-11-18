package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Collections;
import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado {

    protected LinkedList<NodoExpresion> parametros;
    protected Metodo metodoLLamado;

    public NodoMetodoEncadenado(Token tokenNodoEncadenado) {
        super(tokenNodoEncadenado);
        parametros = new LinkedList<>();
        esLLamable=true;
    }


    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    @Override
    public Tipo chequear(Tipo tipo) {
        if(tipo instanceof TipoReferencia){
            Clase clase= TablaDeSimbolos.tablaSimbolos.getClaseByName(tipo.getToken().getLexeme());
            Metodo met;
            if(clase==null){
                Interfaz interfaz =TablaDeSimbolos.tablaSimbolos.getInterfazByName(tipo.getToken().getLexeme());
                met=interfaz.tieneMetodoExacto(tokenNodoEncadenado.getLexeme(),parametros);
            }
            else{
                met=clase.tieneMetodoExacto(tokenNodoEncadenado.getLexeme(),parametros);
            }

            if(met!=null){
                metodoLLamado=met;
                if(nodoEncadenado!=null){
                    Tipo tipoEncadenado=nodoEncadenado.chequear(met.getTipo());
                    if(tipoEncadenado!=null){
                        return tipoEncadenado;
                    }
                    else{
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenNodoEncadenado, "Error Semantico en linea "
                                + tokenNodoEncadenado.getNumberline() + ": El tipo del acceso no puede ser primitivo " +tokenNodoEncadenado.getLexeme()));
                        return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
                    }
                }
                else{
                    return met.getTipo();
                }
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenNodoEncadenado, "Error Semantico en linea "
                        + tokenNodoEncadenado.getNumberline() + ": No existe un metodo con ese nombre o incorrectos parametros " + tokenNodoEncadenado.getLexeme()));

                return new Tipo(new Token("pr_void","void",0)); //Devuelvo esto para que no termine la ejecucion
            }
        }
        else{
            return null;
        }
    }
    @Override
    public void generarCodigo() {
        if (metodoLLamado.isEstatico()){
            TablaDeSimbolos.codigoMaquina.add("POP");
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
        }
        else{
            if (!metodoLLamado.getTipo().compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.codigoMaquina.add("RMEM 1");
                TablaDeSimbolos.codigoMaquina.add("SWAP");
            }
            Collections.reverse(parametros);
            for (NodoExpresion expresion:parametros) {
                expresion.generarCodigo();
                TablaDeSimbolos.codigoMaquina.add("SWAP");
            }
            Collections.reverse(parametros);
            TablaDeSimbolos.codigoMaquina.add("DUP ;duplico this");
            TablaDeSimbolos.codigoMaquina.add("LOADREF 0");
            TablaDeSimbolos.codigoMaquina.add("LOADREF "+metodoLLamado.getOffset());
            TablaDeSimbolos.codigoMaquina.add("CALL");
        }
        if(nodoEncadenado != null){
            if (ladoIzquierdo)
                nodoEncadenado.setLadoIzquierdo();
            nodoEncadenado.generarCodigo();
        }
    }
}
