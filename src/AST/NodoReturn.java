package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoReturn extends NodoSentencia{

    protected NodoExpresion expresion;
    protected Metodo metodoActual;
    protected Token token;

    public NodoReturn(Token token, NodoExpresion expresion, Metodo metodo){
        this.expresion=expresion;
        metodoActual=metodo;
        this.token=token;
    }

    @Override
    public Token getToken() {
        return token;
    }

    public void chequear(){
        if(expresion==null){
           if(!metodoActual.getTipo().getToken().getLexeme().equals("void")){
               TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                       + token.getNumberline() + ": tipo de variable y expresion distintos " + token.getLexeme()));
           }
        }
        else {
            Tipo tipoExpresion=expresion.chequear();
            if(tipoExpresion.compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": tipo de expresion no puede ser void " + token.getLexeme()));
            }
            if(!metodoActual.getTipo().esSubtipo(expresion.chequear(),this)){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": tipo de variable y expresion distintos " + token.getLexeme()));

            }
        }


    }



}
