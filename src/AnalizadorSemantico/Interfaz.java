package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class Interfaz implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, LinkedList<Metodo>> metodos;
    protected Hashtable<String,LinkedList<Token>> interfacesExtendidas;

    public Hashtable<String, Token> getParametrosGenericos() {
        return parametrosGenericos;
    }

    public void setParametrosGenericos(Hashtable<String, Token> parametrosGenericos) {
        this.parametrosGenericos = parametrosGenericos;
    }

    protected Hashtable<String,Token> parametrosGenericos;

    protected boolean consolidada;

    public Interfaz(Token tokenClase) {
        this.tokenClase = tokenClase;
        metodos = new Hashtable<>();
        parametrosGenericos=new Hashtable<>();
        interfacesExtendidas = new Hashtable<>();
        consolidada = false;
    }

    public Token getToken() {
        return tokenClase;
    }


    public void setTokenClase(Token tokenClase) {
        this.tokenClase = tokenClase;
    }

    public Hashtable<String, LinkedList<Metodo>> getMetodos() {
        return metodos;
    }

    public void setMetodos(Hashtable<String, LinkedList<Metodo>> metodos) {
        this.metodos = metodos;
    }


    public boolean isConsolidada() {
        return consolidada;
    }

    public void setConsolidada(boolean consolidada) {
        this.consolidada = consolidada;
    }


    public void agregarMetodo(String nombreMetodo, Metodo metodo) throws SemanticException {
        if (!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo, metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for (Metodo met : metodosMismoNombre) {
                if (met.compareArgumentos(metodo.getListaArgumentos())) {
                    throw new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline()
                            + ": Ya hay un metodo declarado con el nombre " + nombreMetodo + " que posee los mismos argumentos.");
                }
            }
            metodosMismoNombre.add(metodo);
        }

    }

    private void insertarMetodoNuevo(String nombre, Metodo entradaMetodo) {
        LinkedList<Metodo> lista = new LinkedList<Metodo>();
        lista.add(entradaMetodo);
        metodos.put(nombre,lista);
    }


    public void agregarParametro(Token parametro) throws SemanticException {
        if(!parametrosGenericos.containsKey(parametro.getLexeme())){
            parametrosGenericos.put(parametro.getLexeme(),parametro);
        }
        else{
            throw new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme());
        }

    }

    public void agregarInterfaz(LinkedList<Token> interfaz) throws SemanticException {
        if(!interfacesExtendidas.containsKey(interfaz.getFirst().getLexeme())){
           interfacesExtendidas.put(interfaz.getFirst().getLexeme(),interfaz);
        }
        else{
            throw new SemanticException(interfaz.getFirst(), "Error Semantico en linea "
                    + interfaz.getFirst().getNumberline() + ": Ya hay una interfaz declarado con el nombre " + interfaz.getFirst().getLexeme());
        }

    }


}
