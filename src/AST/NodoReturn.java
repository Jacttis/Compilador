package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.MetodoConstructor;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;

public class NodoReturn extends NodoSentencia{

    protected NodoExpresion expresion;
    protected Metodo metodoActual;
    protected Token token;

    public NodoReturn(Token token, NodoExpresion expresion, Metodo metodo){
        this.expresion=expresion;
        metodoActual=metodo;
        this.token=token;
    }

    public void chequear(){
        if(expresion==null){
           if(!metodoActual.getTipo().getToken().getLexeme().equals("void")){
               TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                       + token.getNumberline() + ": tipo de variable y expresion distintos " + token.getLexeme()));
           }
        }
        else {
            if(!metodoActual.getTipo().esSubtipo(expresion.chequear())){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": tipo de variable y expresion distintos " + token.getLexeme()));

            }
        }


    }



}
