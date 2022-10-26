package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

import java.util.Arrays;

public class NodoExpresionBinaria extends NodoExpresion{

    protected Token tokenOp;

    protected NodoExpresion expresionIzq;
    protected  NodoExpresion expresionDer;

    protected Tipo tipoOperador;
    public NodoExpresionBinaria(Token token){
        tokenOp=token;
        if(tokenOp!=null){
            if(Arrays.asList("+","-","*","/","%").contains(token.getDescription())){
                tipoOperador=new TipoPrimitivo(new Token("pr_int","int",0));
                tipoRetorno=new TipoPrimitivo(new Token("pr_int","int",0));
            }
            else if(Arrays.asList("&&","||").contains(token.getDescription())){
                tipoOperador=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                tipoRetorno=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
            }
            else if(Arrays.asList("==","!=").contains(token.getDescription())) {
                tipoOperador=null;
                tipoRetorno=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
            }
            else {
                tipoOperador=new TipoPrimitivo(new Token("pr_int","int",0));
                tipoRetorno=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
            }
        }
    }

    public NodoExpresion getExpresionIzq() {
        return expresionIzq;
    }

    public void setExpresionIzq(NodoExpresion expresionIzq) {
        this.expresionIzq = expresionIzq;
    }

    public NodoExpresion getExpresionDer() {
        return expresionDer;
    }

    public void setExpresionDer(NodoExpresion expresionDer) {
        this.expresionDer = expresionDer;
    }

    @Override
    public Tipo chequear() {
        if(tipoOperador!=null){
            if(expresionDer.chequear().compareTipo(tipoOperador) && expresionIzq.chequear().compareTipo(tipoOperador)){
                return tipoRetorno;
            }
            else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(tokenOp, "Error Semantico en linea "
                        + tokenOp.getNumberline() + ": tipo de operador y operandos distintos " + tokenOp.getLexeme()));
                return tipoRetorno;
            }

        }
        else {
            if(!expresionIzq.chequear().compareTipo(new Tipo(new Token("pr_void","void",0))) && expresionDer.chequear().esSubtipo(expresionIzq.chequear())) {
                return tipoRetorno;
            }
            else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(tokenOp, "Error Semantico en linea "
                        + tokenOp.getNumberline() + ": tipo de operador y operandos incompatibles " + tokenOp.getLexeme()));
                return tipoRetorno;
            }
        }
    }

}
