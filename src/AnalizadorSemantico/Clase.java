package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class Clase implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, Atributo> atributos;
    protected Hashtable<String, LinkedList<Metodo>> metodos;
    protected LinkedList<Token> interfacesImplementadas;
    protected Token claseHerencia;


    protected boolean consolidada;

    public Clase(Token tokenClase) {
        this.tokenClase = tokenClase;
        atributos = new Hashtable<>();
        metodos = new Hashtable<>();
        interfacesImplementadas = new LinkedList<>();
        consolidada = false;
    }

    public Token getToken() {
        return tokenClase;
    }


    public void setTokenClase(Token tokenClase) {
        this.tokenClase = tokenClase;
    }

    public Hashtable<String, Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(Hashtable<String, Atributo> atributos) {
        this.atributos = atributos;
    }

    public Hashtable<String, LinkedList<Metodo>> getMetodos() {
        return metodos;
    }

    public void setMetodos(Hashtable<String, LinkedList<Metodo>> metodos) {
        this.metodos = metodos;
    }

    public LinkedList<Token> getInterfacesImplementadas() {
        return interfacesImplementadas;
    }

    public void setInterfacesImplementadas(LinkedList<Token> interfacesImplementadas) {
        this.interfacesImplementadas = interfacesImplementadas;
    }

    public Token getClaseHerencia() {
        return claseHerencia;
    }

    public void setClaseHerencia(Token claseHerencia) {
        this.claseHerencia = claseHerencia;
    }

    public boolean isConsolidada() {
        return consolidada;
    }

    public void setConsolidada(boolean consolidada) {
        this.consolidada = consolidada;
    }

    public void agregarAtributo(String nombreAtributo,Atributo atributo) throws SemanticException {
        if(!atributos.containsKey(nombreAtributo)){
            atributos.put(nombreAtributo,atributo);
        }
        else{
            throw new SemanticException(atributo.getTokenAtributo(),"Error Semantico en linea "+atributo.getTokenAtributo().getNumberline() +": Ya hay un atributo declarado con el nombre "+nombreAtributo);
        }

    }

    public void agregarMetodo(String nombreMetodo, Metodo metodo) throws SemanticException {
        if(!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo,metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for(Metodo met : metodosMismoNombre){
                if(met.compareArgumentos(metodo.getListaArgumentos())) {
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