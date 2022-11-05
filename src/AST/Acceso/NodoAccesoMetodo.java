package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAcceso{


    protected LinkedList<NodoExpresion> parametros;
    protected Clase clase;
    protected MetodoConstructor metodo;
    public NodoAccesoMetodo(Token token,Clase clase,MetodoConstructor metodo) {
        super(token);
        esLlamable=true;
        this.clase=clase;
        this.metodo=metodo;
    }

    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> expresiones) {
        this.parametros = expresiones;
    }


    @Override
    public Tipo chequear() {
        Metodo met=clase.tieneMetodoExacto(accesoToken.getLexeme(),parametros);
        if(met!=null){
            if(metodo.isEstatico() && !met.isEstatico()){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": No se puede llamar a metodos no estaticos " + accesoToken.getLexeme()));
                return new Tipo(new Token("pr_void","void",0));
            }
            else{
                if(nodoEncadenado!=null){
                    Tipo tipoEncadenado= nodoEncadenado.chequear(met.getTipo());
                    if(tipoEncadenado!=null){
                        return tipoEncadenado;
                    }
                    else{
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                                + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                        return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
                    }
                }
                else{
                    return met.getTipo();
                }

            }
        }
        else{
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                    + accesoToken.getNumberline() + ": No existe un metodo con ese nombre o incorrectos parametros" + accesoToken.getLexeme()));

            return new Tipo(new Token("pr_void","void",0)); //Devuelvo esto para que no termine la ejecucion
        }
    }
}
