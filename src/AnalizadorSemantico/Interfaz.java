package AnalizadorSemantico;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class Interfaz implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, LinkedList<Metodo>> metodos;
    protected boolean herenciaCircularChequeada=false;
    protected Hashtable<String,LinkedList<Token>> interfacesExtendidas;
    private boolean herenciaCircular;

    protected Hashtable<String,Token> parametrosGenericos;

    protected boolean consolidada;

    public Interfaz(Token tokenClase) {
        this.tokenClase = tokenClase;
        metodos = new Hashtable<>();
        parametrosGenericos=new Hashtable<>();
        interfacesExtendidas = new Hashtable<>();
        consolidada = false;
        herenciaCircular=false;
    }
    public Hashtable<String, Token> getParametrosGenericos() {
        return parametrosGenericos;
    }
    public void setParametrosGenericos(Hashtable<String, Token> parametrosGenericos) {
        this.parametrosGenericos = parametrosGenericos;
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
        if (metodo.isEstatico()){
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                    ": Las interfaces no pueden tener metodos estaticos"));
        }
        if (!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo, metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for (Metodo met : metodosMismoNombre) {
                if (met.compareArgumentos(metodo.getListaArgumentos())) {
                    TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                            ": Ya hay un metodo declarado con el nombre " + nombreMetodo + " que posee los mismos argumentos."));
                }
            }
            metodosMismoNombre.add(metodo);
        }

    }

    public void agregarMetodoPadres(String nombreMetodo, Metodo metodo) throws SemanticException {
        if (!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo, metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for (Metodo met : metodosMismoNombre) {
                if (met.compareArgumentos(metodo.getListaArgumentos())) {
                    if(!(met.estatico== metodo.estatico) && !met.getTipo().compareTipo(metodo.getTipo())){
                        TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline()
                                + ": Mal redifinicion del metodo " + nombreMetodo));
                    }
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
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme()));
        }

    }

    @Override
    public void agregarInterfaz(Token token, LinkedList<Token> interfaz) throws SemanticException {
        interfaz.addFirst(token);
        if(!interfacesExtendidas.containsKey(interfaz.getFirst().getLexeme())){
            interfacesExtendidas.put(interfaz.getFirst().getLexeme(),interfaz);
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(interfaz.getFirst(), "Error Semantico en linea "
                    + interfaz.getFirst().getNumberline() + ": Ya hay una interfaz declarado con el nombre " + interfaz.getFirst().getLexeme()));
        }
    }



    public void checkDeclaracion() throws SemanticException {
        for (LinkedList<Token> listaPadres :interfacesExtendidas.values()) {
            Token t=listaPadres.getFirst();
            if (!TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(listaPadres.getFirst().getLexeme())){
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(t, "Error Semantico en linea "
                        + t.getNumberline() + ": No existe una Clase declarada con el nombre-> " + t.getLexeme()+"Como parametro del padre"));
            }
        }
        Token enError=new Token("","",0);
        if(herenciaCircular(new LinkedList<>(),enError)){
            herenciaCircular=true;
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(enError, "Error Semantico en linea "
                    + enError.getNumberline() + ": Herencia Circular " + enError.getLexeme()));
        }
        for (LinkedList<Metodo> listaMetodo:metodos.values()) {
            for (Metodo metodo:listaMetodo) {
                metodo.checkDeclaracion(this);
            }
        }
    }

    public boolean herenciaCircular(LinkedList<String> padres,Token enError){
        if (!herenciaCircularChequeada) {
            if (!padres.contains(this.tokenClase.getLexeme())) {
                padres.add(tokenClase.getLexeme());
                for (String nameI : interfacesExtendidas.keySet()) {
                    if (TablaDeSimbolos.tablaSimbolos.getInterfaces().containsKey(nameI) && TablaDeSimbolos.tablaSimbolos.getInterfazByName(nameI).herenciaCircular(padres, enError)) {
                        enError.setNumberline(interfacesExtendidas.get(nameI).getFirst().getNumberline());
                        enError.setDescription(interfacesExtendidas.get(nameI).getFirst().getDescription());
                        enError.setLexeme(interfacesExtendidas.get(nameI).getFirst().getLexeme());
                        return true;
                    }
                }

            } else {
                return true;
            }
            herenciaCircularChequeada=true;
            return false;
        }
        return false;
    }


    public void consolidar() throws SemanticException {
        if (!consolidada && !herenciaCircular){
            for (LinkedList<Token> token:interfacesExtendidas.values()) {
                    Interfaz padre = TablaDeSimbolos.tablaSimbolos.getInterfazByName(token.getFirst().getLexeme());
                    if(padre!=null) {
                        padre.consolidar();
                        for (LinkedList<Metodo> listaMetodo : padre.getMetodos().values()) {
                            for (Metodo metodo : listaMetodo) {
                                consolidarMetodo(metodo);
                            }
                        }
                    }
            }

        }
    }

    private void consolidarMetodo(Metodo metodo) throws SemanticException {

            agregarMetodoPadres(metodo.getTokenMetodo().getLexeme(),metodo);

    }

    public boolean esSubtipo(String interfaz) {
        boolean esSubtipo=false;
        if(interfaz.equals(tokenClase.getLexeme() )){
            esSubtipo=true;
        }
        else{
            if(interfacesExtendidas.size()>0) {
                for (String interfazE:interfacesExtendidas.keySet()) {
                    if(TablaDeSimbolos.tablaSimbolos.getInterfazByName(interfazE).esSubtipo(interfaz)){
                        return true;
                    }

                }
            }
        }
        return esSubtipo;
    }

    public Metodo tieneMetodoExacto(String nombreMetodo,LinkedList<NodoExpresion> parametros){
        if(metodos.containsKey(nombreMetodo)) {
            for (Metodo met : metodos.get(nombreMetodo)) {
                LinkedList<Parametro> parametrosMet = met.getListaArgumentos();
                int i = 0;
                boolean iguales = true;
                if (parametros.size() == parametrosMet.size()) {
                    while (iguales && i < parametros.size()) {
                        if (!parametros.get(i).chequear().esSubtipo(parametrosMet.get(i).getTipo())) {

                            iguales = false;
                        }
                        i++;
                    }
                    if (iguales) {
                        return met;
                    }
                }
            }
            return null;
        }
        else {
            return null;
        }

    }
}
