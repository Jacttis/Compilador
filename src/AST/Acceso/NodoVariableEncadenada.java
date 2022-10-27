package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoVariableEncadenada extends NodoEncadenado {
    public NodoVariableEncadenada(Token tokenNodoEncadenado) {
        super(tokenNodoEncadenado);
        esAsignable=true;
    }


    @Override
    public Tipo chequear(Tipo tipo) {
        if(tipo instanceof TipoReferencia){
            Clase clase= TablaDeSimbolos.tablaSimbolos.getClaseByName(tipo.getToken().getLexeme());
            if(clase!=null) {
                Atributo atributo=null;
                if(!esThis) {
                    atributo = clase.tieneAtributoPublico(tokenNodoEncadenado.getLexeme());
                }
                else{
                    atributo = clase.getAtributos().get(tokenNodoEncadenado.getLexeme());
                }
                if (atributo != null) {
                    if (nodoEncadenado != null) {
                        Tipo tipoEncadenado = nodoEncadenado.chequear(atributo.getTipoAtributo());
                        if (tipoEncadenado != null) {
                            return tipoEncadenado;
                        } else {
                            TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenNodoEncadenado, "Error Semantico en linea "
                                    + tokenNodoEncadenado.getNumberline() + ": El tipo del acceso no puede ser primitivo " + tokenNodoEncadenado.getLexeme()));
                            return new Tipo(new Token("pr_void", "void", 0));//Devuelvo para seguir ejecucion
                        }
                    } else {
                        return atributo.getTipoAtributo();
                    }
                } else {
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenNodoEncadenado, "Error Semantico en linea "
                            + tokenNodoEncadenado.getNumberline() + ": No existe un atributo con ese nombre o es privado " + tokenNodoEncadenado.getLexeme()));

                    return tipo; //Devuelvo esto para que no termine la ejecucion
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}
