package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado {

    protected LinkedList<NodoExpresion> parametros;

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
            Metodo met=clase.tieneMetodoExacto(tokenNodoEncadenado.getLexeme(),parametros,false);
            if(met!=null){
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
                        + tokenNodoEncadenado.getNumberline() + ": No existe un metodo con ese nombre o incorrectos parametros o no es estatico " + tokenNodoEncadenado.getLexeme()));

                return new Tipo(new Token("pr_void","void",0)); //Devuelvo esto para que no termine la ejecucion
            }
        }
        else{
            return null;
        }
    }
}
