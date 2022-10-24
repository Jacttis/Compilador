package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoAcceso {

    Token accesoToken;
    NodoEncadenado nodoEncadenado;

    public boolean esAsignable;

    public NodoAcceso(Token token){
        accesoToken=token;
        esAsignable=false;
    }
    public Token getToken() {
        return accesoToken;
    }

    public NodoEncadenado getNodoEncadenado() {
        return nodoEncadenado;
    }

    public void setNodoEncadenado(NodoEncadenado nodoEncadenado) {
        this.nodoEncadenado = nodoEncadenado;
    }

    public Tipo chequear() {
    return null;
    }
}
