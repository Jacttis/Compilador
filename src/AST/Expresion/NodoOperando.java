package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoOperando {

    protected Token tokenOperando;
    protected Tipo tipo;

    public NodoOperando(Token operando){

        tokenOperando=operando;
    }

    public Tipo getTipo() {
        return tipo;
    }
    public Token getToken(){
        return tokenOperando;
    }

    public Tipo chequear() {
        return null;
    }
}
