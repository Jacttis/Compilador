package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

public class NodoEncadenado {

    Token tokenNodoEncadenado;
    NodoEncadenado nodoEncadenado;

    public NodoEncadenado(Token tokenNodoEncadenado) {
        this.tokenNodoEncadenado = tokenNodoEncadenado;
        this.nodoEncadenado = null;
    }

    public NodoEncadenado getNodoEncadenado() {
        return nodoEncadenado;
    }

    public void setNodoEncadenado(NodoEncadenado nodoEncadenado) {
        this.nodoEncadenado = nodoEncadenado;
    }

    public Token getTokenNodoEncadenado() {
        return tokenNodoEncadenado;
    }

    public void setTokenNodoEncadenado(Token tokenNodoEncadenado) {
        this.tokenNodoEncadenado = tokenNodoEncadenado;
    }

    public Tipo chequear(TipoReferencia tipo) {
        return null;
    }
}
