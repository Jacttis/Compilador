package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

public class NodoWhile extends NodoSentencia{
    protected NodoExpresion expresion;
    protected NodoSentencia sentencia;
    protected Tipo tipoBoolean;
    protected Token token;
    public NodoWhile(Token token,NodoExpresion expresion,NodoSentencia sentencia){
        this.expresion=expresion;
        this.sentencia=sentencia;
        tipoBoolean=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
        this.token=token;
    }

    @Override
    public void chequear() {
        if (expresion.chequear().compareTipo(tipoBoolean)){
            sentencia.chequear();
        }
        else{
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                    + token.getNumberline() + ": expresion no es tipo boolean del " + token.getLexeme()));

        }
    }

    @Override
    public void generarCodigo() {
        String etiquetaComienzoWhile="LcomienzoWhile"+TablaDeSimbolos.numcondicion1++;
        String etiquetaFinWhile="LfinWhile"+TablaDeSimbolos.numcondicion2++;
        TablaDeSimbolos.codigoMaquina.add(etiquetaComienzoWhile+":");
        expresion.generarCodigo();
        TablaDeSimbolos.codigoMaquina.add("BF "+etiquetaFinWhile);
        sentencia.generarCodigo();
        TablaDeSimbolos.codigoMaquina.add("JUMP "+etiquetaComienzoWhile);
        TablaDeSimbolos.codigoMaquina.add(etiquetaFinWhile+":");

    }
}
