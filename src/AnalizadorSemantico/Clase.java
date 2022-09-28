package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.LinkedList;

public class Clase implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, Atributo> atributos;
    protected Hashtable<String, LinkedList<Metodo>> metodos;
    protected LinkedList<Constructor> constructores;
    protected Hashtable<Token, Hashtable<String,Token>> interfacesImplementadas;

    public Hashtable<String, Token> getParametrosGenericos() {
        return parametrosGenericos;
    }

    Hashtable<String, Token> parametrosGenericos;
    protected Token claseHerencia;

    public Hashtable<String, Token> getParametrosPadre() {
        return parametrosPadre;
    }

    public void setParametrosPadre(LinkedList<Token> parametrosPadre) throws SemanticException {
        for (Token t:parametrosPadre) {
            if(!this.parametrosPadre.contains(t.getLexeme())){
                this.parametrosPadre.put(t.getLexeme(),t);
            }
            else {
                throw new SemanticException(t, "Error Semantico en linea " +
                        t.getNumberline() + ": Ya hay un parametro del padre con el nombre  "+t.getLexeme());
            }
        }
    }

    protected Hashtable<String,Token> parametrosPadre;


    protected boolean consolidada;

    public Clase(Token tokenClase) {
        this.tokenClase = tokenClase;
        atributos = new Hashtable<>();
        metodos = new Hashtable<>();
        interfacesImplementadas = new Hashtable<>();
        parametrosGenericos=new Hashtable<>();
        parametrosPadre=new Hashtable<>();
        constructores=new LinkedList<>();
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

    public void agregarConstructor(Constructor constructor) throws SemanticException {
        if(constructor.getToken().getLexeme().equals(tokenClase.getLexeme())) {
            for (Constructor con : constructores) {
                if (con.compareArgumentos(constructor.getListaArgumentos())) {
                    throw new SemanticException(constructor.getToken(), "Error Semantico en linea " +
                            constructor.getToken().getNumberline() + ": Ya hay un constructor con los mismos argumentos ");
                }
            }
            constructores.add(constructor);
        }
        else {
            throw new SemanticException(constructor.getToken(), "Error Semantico en linea " +
                    constructor.getToken().getNumberline() + ": No es un constructor valido ");

        }
    }

    public void agregarMetodo(String nombreMetodo, Metodo metodo) throws SemanticException {
        if(!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo,metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for(Metodo met : metodosMismoNombre){
                if(met.compareArgumentos(metodo.getListaArgumentos())) {
                    throw new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                            ": Ya hay un metodo declarado con el nombre " + nombreMetodo + " que posee los mismos argumentos.");
                }
            }
            metodosMismoNombre.add(metodo);
        }
    }

    @Override
    public void agregarParametro(Token parametro) throws SemanticException {
        if(!parametrosGenericos.containsKey(parametro.getLexeme())){
            parametrosGenericos.put(parametro.getLexeme(),parametro);
        }
        else{
            throw new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme());
        }

    }

    @Override
    public void agregarInterfaz(Token token,LinkedList<Token> interfaz) throws SemanticException {
        Hashtable<String,Token> parametrosInterfaz=new Hashtable<>();
        for (Token t:interfaz) {
            if(!parametrosInterfaz.contains(t.getLexeme())){
                parametrosInterfaz.put(t.getLexeme(),t);
            }
            else {
                throw new SemanticException(t, "Error Semantico en linea " +
                        t.getNumberline() + ": Ya hay un parametro de la interfaz"+ token.getLexeme()+"con el nombre  "+t.getLexeme());
            }
        }
        if(!interfacesImplementadas.containsKey(token)){
            interfacesImplementadas.put(token,parametrosInterfaz);
        }
        else{
            throw new SemanticException(token, "Error Semantico en linea "
                    + token.getNumberline() + ": Ya hay una interfaz declarado con el nombre " + token.getLexeme());
        }
    }

    private void insertarMetodoNuevo(String nombre, Metodo entradaMetodo) {
        LinkedList<Metodo> lista = new LinkedList<Metodo>();
        lista.add(entradaMetodo);
        metodos.put(nombre,lista);
    }

    public void checkDeclaracion() throws SemanticException {
        if(!tokenClase.getLexeme().equals("Object")) {
            if (checkClase(claseHerencia,parametrosPadre)) {
                if(!checkCircleHerencia(new LinkedList<>())){
                    throw new SemanticException(claseHerencia, "Error Semantico en linea "
                            + claseHerencia.getNumberline() + ": Herencia Circular " + claseHerencia.getLexeme());
                }
                for (Token token:interfacesImplementadas.keySet()) {
                    if(!checkInterface(token,interfacesImplementadas.get(token))){
                        throw new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": No existe una interfaz declarada con el nombre -> " + token.getLexeme());
                    }
                }
                for (LinkedList<Metodo> listaMetodo:metodos.values()) {
                    for (Metodo metodo:listaMetodo) {
                        metodo.checkDeclaracion(this);
                    }
                }

                for (Atributo atributo:atributos.values()) {
                    atributo.checkDeclaracion(this);
                }
            } else {
                throw new SemanticException(claseHerencia, "Error Semantico en linea "
                        + claseHerencia.getNumberline() + ": No existe una Clase declarada con el nombre -> " + claseHerencia.getLexeme());
            }
        }
    }

    private boolean checkClase(Token clase,Hashtable<String,Token> parametros) throws SemanticException {
        boolean correcto=true;
        if(clase!=null && TablaDeSimbolos.tablaSimbolos.getClases().containsKey(clase.getLexeme())){
            if(parametros.size()==TablaDeSimbolos.tablaSimbolos.getClaseByName(clase.getLexeme()).getParametrosGenericos().size()){
                for (Token t:parametros.values()) {
                    if(!TablaDeSimbolos.tablaSimbolos.getClases().containsKey(t.getLexeme()) && !parametrosGenericos.containsKey(t.getLexeme())){
                        throw new SemanticException(t, "Error Semantico en linea "
                                + t.getNumberline() + ": No existe una Clase declarada con el nombre-> " + t.getLexeme()+"Como parametro del padre");
                    }
                }
            }else {
                throw new SemanticException(clase, "Error Semantico en linea "
                        + clase.getNumberline() + ": Cantidad de parametricos invalidos para " + clase.getLexeme());
            }
        }else {
            correcto=false;
        }
        return correcto;
    }

    private boolean checkInterface(Token clase,Hashtable<String,Token> parametros) throws SemanticException {
        boolean correcto=true;
        if(clase!=null && TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(clase.getLexeme())){
            if(parametros.size()==TablaDeSimbolos.tablaSimbolos.getInterfazByName(clase.getLexeme()).getParametrosGenericos().size()){
                for (Token t:parametros.values()) {
                    if(!TablaDeSimbolos.tablaSimbolos.getClases().containsKey(t.getLexeme()) && !parametrosGenericos.containsKey(t.getLexeme())){
                        throw new SemanticException(t, "Error Semantico en linea "
                                + t.getNumberline() + ": No existe una Clase declarada con el nombre-> " + t.getLexeme()+"Como parametro del padre");
                    }
                }
            }else {
                throw new SemanticException(clase, "Error Semantico en linea "
                        + clase.getNumberline() + ": Cantidad de parametricos invalidos para " + clase.getLexeme());
            }
        }else {
            correcto=false;
        }
        return correcto;
    }
    public boolean checkCircleHerencia(LinkedList<Token> clasesPadres) throws SemanticException {
       if(claseHerencia!=null) {
           if (!clasesPadres.contains(claseHerencia)) {
               clasesPadres.add(claseHerencia);
               return TablaDeSimbolos.tablaSimbolos.getClases().get(claseHerencia.getLexeme()).checkCircleHerencia(clasesPadres);
           }
           else{
              return false;
           }
       }
       return true;
    }
}