package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

public class NodoEncadenado  {

    Token tokenNodoEncadenado;
    NodoEncadenado nodoEncadenado;

    protected boolean esAsignable,esLLamable;

    protected boolean esThis=false;

    public NodoEncadenado(Token tokenNodoEncadenado) {
        this.tokenNodoEncadenado = tokenNodoEncadenado;
        this.nodoEncadenado = null;
        esAsignable=false;
        esLLamable=false;
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

    public Tipo chequear(Tipo tipo) {
        return new Tipo(new Token("pr_int","int",0));
    }

    public boolean esAsignable(){
        if(nodoEncadenado!=null){
            return nodoEncadenado.esAsignable();
        }
        else{
            return esAsignable;
        }
    }

    public void setEsThis(){
        esThis=true;
    }

   /* public boolean isLLamable() {
        if(nodoEncadenado!=null){
            return nodoEncadenado.isLLamable();
        }
        else{
            return esLLamable;
        }
    }*/

    public Object[] isLLamable() {
        Object[] retorno=new Object[2];
        if(nodoEncadenado!=null){
            return nodoEncadenado.isLLamable();
        }
        else{
            retorno[0]=esLLamable;
            retorno[1]=tokenNodoEncadenado;
            return retorno;
        }
    }
}
