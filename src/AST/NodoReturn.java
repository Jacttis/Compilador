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
                       + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
           }
        }
        else {
            Tipo tipoExpresion=expresion.chequear();
            if(tipoExpresion.compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": tipo de expresion no puede ser void " + token.getLexeme()));
            }else{
                if (!tipoExpresion.compareTipo(new Tipo(new Token("pr_null","null",0)))){
                    if(!metodoActual.getTipo().esSubtipo(tipoExpresion,this)){
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
                    }
                }
                else {
                    if(!(metodoActual.getTipo() instanceof TipoReferencia)){
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
                    }
                }

            }
        }


    }



}
