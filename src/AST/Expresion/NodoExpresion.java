package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoExpresion {

    protected Tipo tipoRetorno;

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
    }



    protected Token operador;

    public void setTipo(Tipo tipo){
        tipoRetorno =tipo;
    }
    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }



    public boolean esSubtipo(Tipo tipo){
        return tipoRetorno.esSubtipo(tipo);
    }

    public Tipo chequear() {

    return null;
    }

    public void generarCodigo() {
    }
}
