package AST;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    protected LinkedList<NodoSentencia> listaSentencias;
    protected Hashtable<String, NodoVarLocal> varLocales;
    protected MetodoConstructor metodoBloque;
    protected NodoBloque bloqueContainer;

    public  boolean chequeado;
    private boolean tieneRetorno;

    protected int ultimoOffset;

    public NodoBloque(MetodoConstructor metodoConstructor){
        metodoBloque=metodoConstructor;
        listaSentencias=new LinkedList<>();
        varLocales=new Hashtable<>();
        chequeado=false;
        ultimoOffset=0;
    }
    public void addSentencia(NodoSentencia sentencia){
        listaSentencias.add(sentencia);
    }

    public void addVarLocal(NodoVarLocal varLocal) throws SemanticException {
        actualizarUltimoOffset();
        if(!varLocales.containsKey(varLocal.getToken().getLexeme())){
            varLocal.offset=ultimoOffset--;
            varLocales.put(varLocal.getToken().getLexeme(),varLocal);
        }
        else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(varLocal.getToken(), "Error Semantico en linea "
                    + varLocal.getToken().getNumberline() + ": Ya hay una var local declarado con el nombre " + varLocal.getToken().getLexeme()));
        }
    }

    public int actualizarUltimoOffset() {
        if(bloqueContainer != null && bloqueContainer.actualizarUltimoOffset()<ultimoOffset){
            ultimoOffset= bloqueContainer.actualizarUltimoOffset();
        }
        return ultimoOffset;
    }

    public int obtenerSizeVarLocal(){
        if(bloqueContainer!=null){
            int cantidadVarLocal=varLocales.size()+ bloqueContainer.obtenerSizeVarLocal();
            return cantidadVarLocal;
        }
        else {
            return varLocales.size();
        }
    }

    public LinkedList<NodoSentencia> getListaSentencias() {
        return listaSentencias;
    }

    public void setListaSentencias(LinkedList<NodoSentencia> listaSentencias) {
        this.listaSentencias = listaSentencias;
    }

    public Hashtable<String, NodoVarLocal> getVarLocales() {
        return varLocales;
    }

    public void setVarLocales(Hashtable<String, NodoVarLocal> varLocales) {
        this.varLocales = varLocales;
    }

    public MetodoConstructor getMetodoBloque() {
        return metodoBloque;
    }

    public void setMetodoBloque(MetodoConstructor metodoBloque) {
        this.metodoBloque = metodoBloque;
    }

    public NodoBloque getBloqueContainer() {
        return bloqueContainer;
    }

    public void setBloqueContainer(NodoBloque bloqueContainer) {
        this.bloqueContainer = bloqueContainer;
    }

    @Override
    public void chequear() {
        chequearBloque();
    }

    public void chequearBloque() {
        if(!chequeado){
            for (NodoVarLocal varLocal: varLocales.values()) {
                varLocal.chequear();
            }
            for (NodoSentencia sentencia:listaSentencias) {
                if(sentencia==null){
                    //nada porque es punto y coma
                }
                else{
                    sentencia.chequear();
                }

            }
            chequeado=true;
        }
        else{

        }
    }

    public boolean estaVarEnBloque(Token token){
        if(bloqueContainer!=null){
            return varLocales.containsKey(token.getLexeme()) && token.getNumberline()>varLocales.get(token.getLexeme()).getToken().getNumberline() || bloqueContainer.estaVarEnBloque(token);
        }
        else{
            return varLocales.containsKey(token.getLexeme()) && token.getNumberline()>varLocales.get(token.getLexeme()).getToken().getNumberline() ;
        }

    }

    public NodoVarLocal getVarEnBloque(Token token){
        if(bloqueContainer!=null){
            NodoVarLocal varLocal=varLocales.get(token.getLexeme());
            varLocal = varLocal == null ? bloqueContainer.getVarEnBloque(token) : varLocal;
            return varLocal;
        }
        else{
            return varLocales.get(token.getLexeme());
        }
    }

    public Tipo tipoVarEnBloque(Token token){
        if(bloqueContainer!=null){
            if(varLocales.containsKey(token.getLexeme())) {
                return varLocales.get(token.getLexeme()).getTipo();
            }
             return bloqueContainer.tipoVarEnBloque(token);
        }
        else{
            if(varLocales.containsKey(token.getLexeme())) {
                return varLocales.get(token.getLexeme()).getTipo();
            }
            else {
                return null;
            }
        }

    }

    @Override
    public void generarCodigo() {
        //TablaDeSimbolos.codigoMaquina.add("RMEM "+ varLocales.size());
        for (NodoSentencia sentencia:listaSentencias) {
            sentencia.generarCodigo();
        }
        if(bloqueContainer!=null){
            TablaDeSimbolos.codigoMaquina.add("FMEM "+ varLocales.size());
            ultimoOffset+= varLocales.size();
        }
        else{
            TablaDeSimbolos.codigoMaquina.add("FMEM "+ obtenerSizeVarLocal());
            ultimoOffset+= obtenerSizeVarLocal();
        }

    }
}
