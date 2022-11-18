package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoIf extends NodoSentencia{
    protected NodoExpresion expresion;
    protected NodoSentencia sentencia;
    protected NodoElse nodoElse;

    protected Tipo tipoBoolean;
    protected Token token;

    public NodoIf(Token token,NodoExpresion expresion,NodoSentencia sentencia){
        this.expresion=expresion;
        this.sentencia=sentencia;
        tipoBoolean=new TipoPrimitivo(new Token("pr_boolean","boolean",0));
        this.token=token;
    }

    public NodoElse getNodoElse() {
        return nodoElse;
    }

    public void setNodoElse(NodoElse nodoElse) {
        this.nodoElse = nodoElse;
    }

    @Override
    public void chequear() {
        if (expresion.chequear().compareTipo(tipoBoolean)){
            if (sentencia!=null){
                sentencia.chequear();
                if (nodoElse!=null){
                    nodoElse.chequear();
                }
            }
        }
        else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
            + token.getNumberline() + ": expresion no es tipo boolean del " + token.getLexeme()));
        }
    }

    @Override
    public void generarCodigo() {
        String etiquetaIF="Lif"+TablaDeSimbolos.numcondicion1++;
        String etiquetaElse="Lelse"+TablaDeSimbolos.numcondicion2++;
        expresion.generarCodigo();
        if(nodoElse!=null){
            TablaDeSimbolos.codigoMaquina.add("BF "+etiquetaElse+" ; si el tope es falso salto a "+etiquetaElse);
            sentencia.generarCodigo();
            TablaDeSimbolos.codigoMaquina.add("JUMP "+etiquetaIF);
            TablaDeSimbolos.codigoMaquina.add(etiquetaElse+":");
            nodoElse.generarCodigo();

            TablaDeSimbolos.codigoMaquina.add(etiquetaIF+": NOP");
        }
        else {
            TablaDeSimbolos.codigoMaquina.add("BF "+etiquetaIF+" ; si el tope es falso salto a "+etiquetaIF);
            if(sentencia!=null) {
                sentencia.generarCodigo();
            }
            TablaDeSimbolos.codigoMaquina.add(etiquetaIF+": NOP");
        }
    }
}

