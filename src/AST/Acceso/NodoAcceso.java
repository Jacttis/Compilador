package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoAcceso {

    Token accesoToken;
    NodoEncadenado nodoEncadenado;

    public boolean esAsignable,esLlamable;

    public NodoAcceso(Token token){
        accesoToken=token;
        esAsignable=false;
        esLlamable=false;
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
    } //esto hace que sea nulo cuando no esta definido el chequear
    public boolean esAsignable(){
        if(nodoEncadenado!=null){
            return nodoEncadenado.esAsignable();
        }
        else{
            return esAsignable;
        }
    }

    public boolean isLLamable() {
        if(nodoEncadenado!=null){
            return nodoEncadenado.esAsignable();
        }
        else{
            return esAsignable;
        }
    }
}
