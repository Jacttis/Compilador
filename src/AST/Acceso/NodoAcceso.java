package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

import java.util.Map;

public class NodoAcceso {

    Token accesoToken;
    NodoEncadenado nodoEncadenado;

    public boolean esAsignable,esLlamable,ladoIzquierdo;

    public NodoAcceso(Token token){
        accesoToken=token;
        esAsignable=false;
        esLlamable=false;
        ladoIzquierdo=false;
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

    public Object[] isLLamable() {
        Object[] retorno=new Object[2];
        if(nodoEncadenado!=null){
            return nodoEncadenado.isLLamable();
        }
        else{
            retorno[0]=esLlamable;
            retorno[1]=accesoToken;
            return retorno;
        }
    }

    public void genererarCodigo() {
    }

    public void setLadoIzquierdo(){
        ladoIzquierdo=true;
    }

    public boolean isLadoIzquierdo(){
        return ladoIzquierdo;
    }
}
