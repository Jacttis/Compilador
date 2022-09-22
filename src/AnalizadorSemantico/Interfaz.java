package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class Interfaz implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, LinkedList<Metodo>> metodos;
    protected LinkedList<Token> interfacesExtendidas;

    protected boolean consolidada;

    public Interfaz(Token tokenClase) {
        this.tokenClase = tokenClase;
        metodos = new Hashtable<>();
        interfacesExtendidas = new LinkedList<>();
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

    public LinkedList<Token> getInterfacesImplementadas() {
        return interfacesExtendidas;
    }

    public void setInterfacesImplementadas(LinkedList<Token> interfacesImplementadas) {
        this.interfacesExtendidas = interfacesImplementadas;
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
                    throw new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() + ": Ya hay un metodo declarado con el nombre " + nombreMetodo + " que posee los mismos argumentos.");
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


}
