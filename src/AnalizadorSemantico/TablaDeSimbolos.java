package AnalizadorSemantico;

import AST.NodoBloque;
import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public class TablaDeSimbolos {
    protected Hashtable<String,Clase> clases;
    protected Hashtable<String, Interfaz> interfaces;
    protected IClaseInterfaz claseActual;
    protected MetodoConstructor metodoActual;
    public static TablaDeSimbolos tablaSimbolos;
    public Metodo main;

    public static LinkedList<Exception> listaExcepciones=new LinkedList<>();
    private NodoBloque bloqueActual;

    public TablaDeSimbolos() throws SemanticException {
        clases=new Hashtable<>();
        interfaces=new Hashtable<>();
        tablaSimbolos=this;
        createObject();
        createSystem();
        createString();
    }

    public void agregarClase(String nombre,Clase clase) throws SemanticException {
        if(!interfaces.containsKey(nombre) && !clases.containsKey(nombre)){
            clases.put(nombre,clase);
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(clase.getToken(),"La clase "+nombre+" ya esta declarada"));
        }
    }
    public void agregarInterfaz(String nombre,Interfaz interfaz) throws SemanticException {
        if(!interfaces.containsKey(nombre) && !clases.containsKey(nombre)){
            interfaces.put(nombre,interfaz);
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(interfaz.getToken(),"La clase "+nombre+" ya esta declarada"));
        }
    }

    public Hashtable<String, Clase> getClases() {
        return clases;
    }

    public void setClases(Hashtable<String, Clase> clases) {
        this.clases = clases;
    }

    public Hashtable<String, Interfaz> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Hashtable<String, Interfaz> interfaces) {
        this.interfaces = interfaces;
    }

    public IClaseInterfaz getClaseActual() {
        return claseActual;
    }

    public void setClaseActual(IClaseInterfaz claseActual) {
        this.claseActual = claseActual;
    }


    public MetodoConstructor getMetodoActual() {
        return metodoActual;
    }

    public void setMetodoActual(MetodoConstructor metodoActual) {
        this.metodoActual = metodoActual;
    }

    public Clase getClaseByName(String name){
        return clases.get(name);
    }

    public void checkDeclaracion() throws SemanticException {
        for (Clase c:clases.values()) {
            c.checkDeclaracion();
        }
        for (Interfaz i:interfaces.values()) {
            i.checkDeclaracion();
        }

        if (main==null){
            Token sinMain=new Token("","",0);
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(sinMain, "Error Semantico en linea "
                    + sinMain.getNumberline() + ": Clase Main no declarada " + sinMain.getLexeme()));
        }
    }

    public void consolidar() throws SemanticException {
        for (Interfaz i:interfaces.values()) {
            i.consolidar();
        }

        for(Clase c: clases.values()){
            c.consolidar();
        }
    }

    public void chequearBloques(){
        for (Clase c:clases.values()) {
            for (LinkedList<Metodo> metodo:c.getMetodos().values()) {
                for (Metodo met:metodo) {
                    met.chequearBloque();
                }
            }
        }
    }

    private void createString(){
        Clase system = new Clase(new Token("idClase","String",0));
        system.setClaseHerencia(clases.get("Object").getToken());
        clases.put(system.getToken().getLexeme(), system);
    }

    private void createObject() throws SemanticException {
        Clase object = new Clase(new Token("idClase","Object",0));
        Metodo debugPrint = new Metodo(new Token("idMetVar","debugPrint",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
        Parametro i = new Parametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
        debugPrint.addArgumento(i);
        debugPrint.setBloquePrincipal(new NodoBloque(debugPrint));
        object.agregarMetodo("debugPrint", debugPrint);
        clases.put(object.getToken().getLexeme(), object);

    }
    private void createSystem() throws SemanticException {
        Clase system = new Clase(new Token("idClase","System",0));
        system.setClaseHerencia(clases.get("Object").getToken());

            Metodo read = new Metodo(new Token("idMetVar", "read", 0), true, new TipoPrimitivo(new Token("pr_int", "int", 0)));
            read.setBloquePrincipal(new NodoBloque(read));
            read.bloquePrincipal.chequeado=true;
            system.agregarMetodo(read.getTokenMetodo().getLexeme(), read);


            Metodo printB = new Metodo(new Token("idMetVar","printB",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro b = new Parametro(new Token("idMetVar","b",0),new TipoPrimitivo(new Token("pr_boolean","boolean",0)));
            printB.addArgumento(b);
            printB.setBloquePrincipal(new NodoBloque(printB));
            system.agregarMetodo(printB.getTokenMetodo().getLexeme(),printB);


            Metodo printC = new Metodo(new Token("idMetVar","printC",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro c = new Parametro(new Token("idMetVar","c",0),new TipoPrimitivo(new Token("pr_char","char",0)));
            printC.addArgumento(c);
            printC.setBloquePrincipal(new NodoBloque(printC));
            system.agregarMetodo(printC.getTokenMetodo().getLexeme(), printC);


            Metodo printI = new Metodo(new Token("idMetVar","printI",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro i = new Parametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
            printI.addArgumento(i);
            printI.setBloquePrincipal(new NodoBloque(printI));
            system.agregarMetodo(printI.getTokenMetodo().getLexeme(),printI);


            Metodo printS = new Metodo(new Token("idMetVar","printS",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro s = new Parametro(new Token("idMetVar","s",0),new TipoPrimitivo(new Token("pr_String","String",0)));
            printS.addArgumento(s);
            printS.setBloquePrincipal(new NodoBloque(printS));
            system.agregarMetodo(printS.getTokenMetodo().getLexeme(),printS);


            Metodo println = new Metodo(new Token("idMetVar","println",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            println.setBloquePrincipal(new NodoBloque(println));
            system.agregarMetodo(println.getTokenMetodo().getLexeme(),println);


            Metodo printBln = new Metodo(new Token("idMetVar","printBln",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro b2 = new Parametro(new Token("idMetVar","b",0),new TipoPrimitivo(new Token("pr_boolean","boolean",0)));
            printBln.addArgumento(b2);
            printBln.setBloquePrincipal(new NodoBloque(printBln));
            system.agregarMetodo(printBln.getTokenMetodo().getLexeme(),printBln);


            Metodo printCln = new Metodo(new Token("idMetVar","printCln",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro c2 = new Parametro(new Token("idMetVar","c",0),new TipoPrimitivo(new Token("pr_char","char",0)));
            printCln.addArgumento(c2);
            printCln.setBloquePrincipal(new NodoBloque(printCln));
            system.agregarMetodo(printCln.getTokenMetodo().getLexeme(),printCln);


            Metodo printIln = new Metodo(new Token("idMetVar","printIln",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro i2 = new Parametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
            printIln.addArgumento(i2);
            printIln.setBloquePrincipal(new NodoBloque(printIln));
            system.agregarMetodo(printIln.getTokenMetodo().getLexeme(),printIln);


            Metodo printSln = new Metodo(new Token("idMetVar","printSln",0),true,new TipoPrimitivo(new Token("pr_void","void",0)));
            Parametro s2 = new Parametro(new Token("idMetVar","s",0),new TipoPrimitivo(new Token("pr_String","String",0)));
            printSln.addArgumento(s2);
            printSln.setBloquePrincipal(new NodoBloque(printSln));
            system.agregarMetodo(printSln.getTokenMetodo().getLexeme(),printSln);

            clases.put(system.getToken().getLexeme(), system);


        }


    public Interfaz getInterfazByName(String lexeme) {
        return interfaces.get(lexeme);
    }

    public void setMetodoMain(Metodo metodo) {
            main=metodo;
    }

    public void setBloqueActual(NodoBloque bloqueMetodoActual) {
        bloqueActual=bloqueMetodoActual;
    }
    public NodoBloque getBloqueActual(){return bloqueActual;}
}
