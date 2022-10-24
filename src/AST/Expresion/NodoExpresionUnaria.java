package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

import java.util.Arrays;

public class NodoExpresionUnaria extends NodoExpresion {

    protected NodoOperando operando;
    protected Tipo tipoOperador;

    public NodoExpresionUnaria(Token operador, NodoOperando operando){
        this.operador = operador;
        this.operando = operando;
        if(operador !=null){
            if(Arrays.asList("+","-").contains(operador.getDescription())){
                tipoOperador=new TipoPrimitivo(new Token("pr_int","int",0));
            }
            else{
                tipoOperador=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
            }
        }
    }

    public Tipo chequear(){
        if(operador !=null){
            if (operando.chequear().compareTipo(tipoOperador)){
                return tipoOperador;
            }
            else{
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(operando.getToken(), "Error Semantico en linea "
                        + operando.getToken().getNumberline() + ": tipo de operador y operando distintos " + operando.getToken().getLexeme()));
                return tipoOperador;
            }
        }
        else {
            return operando.chequear();
        }

    }
}
