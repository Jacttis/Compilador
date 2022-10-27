package AnalizadorSemantico;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class Clase implements IClaseInterfaz{

    protected Token tokenClase;
    protected Hashtable<String, Atributo> atributos;
    protected Hashtable<String, LinkedList<Metodo>> metodos;

    protected LinkedList<Constructor> constructores;
    protected Hashtable<Token, Hashtable<String,Token>> interfacesImplementadas;
    private boolean herenciaCircular;

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
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(t, "Error Semantico en linea " +
                        t.getNumberline() + ": Ya hay un parametro del padre con el nombre  "+t.getLexeme()));
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
        herenciaCircular=false;
    }

    public Token getToken() {
        return tokenClase;
    }

    public LinkedList<Constructor> getConstructores() {
        return constructores;
    }

    public void setConstructores(LinkedList<Constructor> constructores) {
        this.constructores = constructores;
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
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(atributo.getTokenAtributo(),"Error Semantico en linea "+atributo.getTokenAtributo().getNumberline() +": Ya hay un atributo declarado con el nombre "+nombreAtributo));
        }

    }

    public void agregarConstructor(Constructor constructor) throws SemanticException {
        if(constructor.getToken().getLexeme().equals(tokenClase.getLexeme())) {
            for (Constructor con : constructores) {
                if (con.compareArgumentos(constructor.getListaArgumentos())) {
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(constructor.getToken(), "Error Semantico en linea " +
                            constructor.getToken().getNumberline() + ": Ya hay un constructor con los mismos argumentos "));
                }
            }
            constructores.add(constructor);
        }
        else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(constructor.getToken(), "Error Semantico en linea " +
                    constructor.getToken().getNumberline() + ": No es un constructor valido "));

        }
    }

    public void agregarMetodo(String nombreMetodo, Metodo metodo) throws SemanticException {
        if (nombreMetodo.equals("main") && metodo.isEstatico() ){
            if(metodo.getListaArgumentos().size()>0){
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                        ": El metodo main no acepta parametros "));
            }
            if ( TablaDeSimbolos.tablaSimbolos.main==null) {
                TablaDeSimbolos.tablaSimbolos.setMetodoMain(metodo);
            }
            else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                        ": Ya hay un metodo main declarado "));
            }
        }
        if(!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo,metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for(Metodo met : metodosMismoNombre){
                if(met.compareArgumentos(metodo.getListaArgumentos())) {
                    TablaDeSimbolos.listaExcepciones.add( new SemanticException(metodo.getTokenMetodo(), "Error Semantico en linea " + metodo.getTokenMetodo().getNumberline() +
                            ": Ya hay un metodo declarado con el nombre " + nombreMetodo + " que posee los mismos argumentos."));
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
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(parametro, "Error Semantico en linea "
                    + parametro.getNumberline() + ": Ya hay un parametro declarado con el nombre " + parametro.getLexeme()));
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
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(t, "Error Semantico en linea " +
                        t.getNumberline() + ": Ya hay un parametro de la interfaz"+ token.getLexeme()+"con el nombre  "+t.getLexeme()));
            }
        }
        if(!this.contieneA(token)){
            interfacesImplementadas.put(token,parametrosInterfaz);
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(token, "Error Semantico en linea "
                    + token.getNumberline() + ": Ya hay una interfaz declarado con el nombre " + token.getLexeme()));
        }
    }

    private boolean contieneA(Token token){
        for (Token t:interfacesImplementadas.keySet()) {
            if(t.getLexeme().equals(token.getLexeme())){
                return true;
            }
        }
        return false;
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
                    herenciaCircular=true;
                    TablaDeSimbolos.listaExcepciones.add( new SemanticException(claseHerencia, "Error Semantico en linea "
                            + claseHerencia.getNumberline() + ": Herencia Circular " + claseHerencia.getLexeme()));
                }
                for (Token token:interfacesImplementadas.keySet()) {
                    if(!checkInterface(token,interfacesImplementadas.get(token))){
                        TablaDeSimbolos.listaExcepciones.add( new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": No existe una interfaz declarada con el nombre -> " + token.getLexeme()));
                    }
                }
                for (LinkedList<Metodo> listaMetodo:metodos.values()) {
                    for (Metodo metodo:listaMetodo) {
                        metodo.checkDeclaracion(this);
                    }
                }

                for (Constructor constructor: constructores) {
                    constructor.checkDeclaracion(this);
                }

                if (constructores.isEmpty()){
                    Constructor constructorPredeterminado = new Constructor(new Token("idClase",tokenClase.getLexeme(),tokenClase.getNumberline()));
                    agregarConstructor(constructorPredeterminado);
                }

                for (Atributo atributo:atributos.values()) {
                    atributo.checkDeclaracion(this);
                }
            } else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(claseHerencia, "Error Semantico en linea "
                        + claseHerencia.getNumberline() + ": No existe una Clase declarada con el nombre -> " + claseHerencia.getLexeme()));
            }
        }
    }

    private boolean checkClase(Token clase,Hashtable<String,Token> parametros) throws SemanticException {
        boolean correcto=true;
        if(clase!=null && TablaDeSimbolos.tablaSimbolos.getClases().containsKey(clase.getLexeme())){
            if(parametros.size()==TablaDeSimbolos.tablaSimbolos.getClaseByName(clase.getLexeme()).getParametrosGenericos().size()){
                for (Token t:parametros.values()) {
                    if(!TablaDeSimbolos.tablaSimbolos.getClases().containsKey(t.getLexeme()) && !parametrosGenericos.containsKey(t.getLexeme())){
                        TablaDeSimbolos.listaExcepciones.add( new SemanticException(t, "Error Semantico en linea "
                                + t.getNumberline() + ": No existe una Clase declarada con el nombre-> " + t.getLexeme()+"Como parametro del padre"));
                    }
                }
            }else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(clase, "Error Semantico en linea "
                        + clase.getNumberline() + ": Cantidad de parametricos invalidos para " + clase.getLexeme()));
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
                        TablaDeSimbolos.listaExcepciones.add( new SemanticException(t, "Error Semantico en linea "
                                + t.getNumberline() + ": No existe una Clase declarada con el nombre-> " + t.getLexeme()+"Como parametro del padre"));
                    }
                }
            }else {
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(clase, "Error Semantico en linea "
                        + clase.getNumberline() + ": Cantidad de parametricos invalidos para " + clase.getLexeme()));
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

    public boolean esSubtipo(String clase){
        boolean esSubtipo=false;
        if(clase.equals(tokenClase.getLexeme() )|| contieneA(new Token("pr_class",clase,0))){
            esSubtipo=true;
        }
        else{
            if(claseHerencia!=null) {
                esSubtipo = TablaDeSimbolos.tablaSimbolos.getClaseByName(claseHerencia.getLexeme()).esSubtipo(clase);
            }
        }
        return esSubtipo;
    }


    public void consolidar() throws SemanticException {
        if (!consolidada && !herenciaCircular) {
            if (claseHerencia != null ) {
                Clase padre = TablaDeSimbolos.tablaSimbolos.getClaseByName(claseHerencia.getLexeme());
                if(padre!=null) {
                    padre.consolidar();
                    for (LinkedList<Metodo> listaMetodo : padre.getMetodos().values()) {
                        for (Metodo metodo : listaMetodo) {
                            consolidarMetodo(metodo);
                        }
                    }
                    for (Atributo atributo : padre.getAtributos().values()) {
                        if(atributo.getVisibilidadAtributo().equals("public")) {
                            agregarAtributoPadre(atributo.getTokenAtributo().getLexeme(), atributo);
                        }
                    }
                }
            }
            for (Token tokensInterfaces:interfacesImplementadas.keySet()) {
                    Interfaz interfazPadre = TablaDeSimbolos.tablaSimbolos.getInterfazByName(tokensInterfaces.getLexeme());
                    if(interfazPadre!=null) {
                        for (LinkedList<Metodo> listaMetodos : interfazPadre.getMetodos().values()) {
                            for (Metodo metodo : listaMetodos) {
                                if (metodos.containsKey(metodo.getTokenMetodo().getLexeme())) {
                                    for (Metodo metodoMismoNombre : metodos.get(metodo.getTokenMetodo().getLexeme())) {
                                        if (!metodoMismoNombre.compareArgumentos(metodo.getListaArgumentos())) {
                                            TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokensInterfaces, "Error Semantico en linea "
                                                    + tokensInterfaces.getNumberline() + ": No implementa los metodos de la interfaz " + tokensInterfaces.getLexeme()));
                                        }
                                    }
                                } else {
                                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokensInterfaces, "Error Semantico en linea "
                                            + tokensInterfaces.getNumberline() + ": No implementa los metodos de la interfaz " + tokensInterfaces.getLexeme()));
                                }
                            }

                        }
                    }
            }

        }
        
    }

    private void consolidarMetodo(Metodo metodo) throws SemanticException {

        agregarMetodoPadres(metodo.getTokenMetodo().getLexeme(),metodo);
    }

    private void agregarMetodoPadres(String nombreMetodo, Metodo metodo) throws SemanticException {
        boolean redefinido=false;
        if (!metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo, metodo);
        else {
            LinkedList<Metodo> metodosMismoNombre = metodos.get(nombreMetodo);
            for (Metodo met : metodosMismoNombre) {
                if (met.compareArgumentos(metodo.getListaArgumentos())) {
                    if(!(met.estatico== metodo.estatico) || !metodo.getTipo().compareTipo(met.getTipo())){
                        TablaDeSimbolos.listaExcepciones.add( new SemanticException(met.getTokenMetodo(), "Error Semantico en linea " + met.getTokenMetodo().getNumberline()
                                + ": Mal redifinicion del metodo " + met.getTokenMetodo().getLexeme()));
                    }
                    else{
                        redefinido=true;
                    }
                }
            }
            if (!redefinido)
                metodosMismoNombre.add(metodo);
        }
    }


    private void agregarAtributoPadre(String nombreAtributo,Atributo atributo) throws SemanticException {
        if(!atributos.containsKey(nombreAtributo)){
            atributos.put(nombreAtributo,atributo);
        }
        else{
            //nada para tapar atributo
        }

    }

    public boolean chequearContieneConstructor(LinkedList<NodoExpresion> parametros){
        for (Constructor constructor:constructores) {
            LinkedList<Parametro> parametrosC=constructor.getListaArgumentos();
            int i=0;
            boolean iguales=true;
            if (parametros.size()==parametrosC.size()){
                while(iguales && i<parametros.size()){
                    if(!parametrosC.get(i).getTipo().esSubtipo(parametros.get(i).chequear())){
                        iguales=false;
                    }
                    i++;
                }
                if(iguales){
                    return true;
                }
            }
        }
        return false;

    }

    public Atributo tieneAtributoPublico(String atributo){
        Atributo atr=atributos.get(atributo);
        if(atr!=null && atr.getVisibilidadAtributo().equals("public")){
            return atr;
        }
        else{
            return null;
        }
    }


    public Metodo tieneMetodoExacto(String nombreMetodo,LinkedList<NodoExpresion> parametros,boolean estatico){
        if(metodos.containsKey(nombreMetodo)) {
            for (Metodo met : metodos.get(nombreMetodo)) {
                LinkedList<Parametro> parametrosMet = met.getListaArgumentos();
                int i = 0;
                boolean iguales = true;
                if (parametros.size() == parametrosMet.size()) {
                    while (iguales && i < parametros.size()) {
                        if (!parametrosMet.get(i).getTipo().esSubtipo(parametros.get(i).chequear())) {
                            iguales = false;
                        }
                        i++;
                    }
                    if (iguales) {
                        if(estatico== met.isEstatico()) {
                            return met;
                        }
                        else{
                            return null;
                        }
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