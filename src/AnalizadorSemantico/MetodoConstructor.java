package AnalizadorSemantico;

import AST.NodoBloque;
import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class MetodoConstructor {

    protected LinkedList<Parametro> listaArgumentos;
    protected Token token;

    protected NodoBloque bloquePrincipal;
    protected int offsetDisponibleParametro;


    public MetodoConstructor(){
        listaArgumentos = new LinkedList<Parametro>();
    }

    public abstract void addArgumento(Parametro argumento) throws SemanticException;

    public boolean compareArgumentos(LinkedList<Parametro> parametrosToCompare){
        boolean iguales = true;
        if(parametrosToCompare.size() == listaArgumentos.size()) {
            for (int i = 0; i < parametrosToCompare.size() && iguales; i++){
                iguales = parametrosToCompare.get(i).toCompare(listaArgumentos.get(i));
            }
        }
        else iguales = false;

        return iguales;
    }

    public boolean repiteNombre(Token token){
        for (Parametro param:listaArgumentos) {
            if(token.getLexeme().equals(param.getToken().getLexeme())){
                return true;
            }
        }
        return false;
    }

    public Parametro getParam(Token token){
        for (Parametro param:listaArgumentos) {
            if(token.getLexeme().equals(param.getToken().getLexeme())){
                return param;
            }
        }
        return null;
    }

    public Tipo tipoParametro(Token token){
        for (Parametro param:listaArgumentos) {
            if(token.getLexeme().equals(param.getToken().getLexeme())){
                return param.getTipo();
            }
        }
        return null;
    }


    public LinkedList<Parametro> getListaArgumentos() {  return listaArgumentos;}

    public NodoBloque getBloquePrincipal() {
        return bloquePrincipal;
    }

    public void chequearBloque() {
        bloquePrincipal.chequearBloque();
    }
    public Tipo getTipo(){
        return new Tipo(new Token("pr_void","void",0));
    }

    public boolean isEstatico(){
        return false;
    }
    public void setBloquePrincipal(NodoBloque bloquePrincipal) {
        this.bloquePrincipal = bloquePrincipal;
    }
}
