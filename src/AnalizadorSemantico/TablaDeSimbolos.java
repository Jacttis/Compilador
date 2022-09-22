package AnalizadorSemantico;

import java.util.Hashtable;

public class TablaDeSimbolos {
    protected Hashtable<String,Clase> clases;
    protected Hashtable<String, Interfaz> interfaces;
    protected IClaseInterfaz claseActual;
    protected Metodo metodoActual;
    public static TablaDeSimbolos tablaSimbolos;

    public TablaDeSimbolos(){
        clases=new Hashtable<>();
        interfaces=new Hashtable<>();
        tablaSimbolos=this;
    }

    public void agregarClase(String nombre,Clase clase) throws SemanticException {
        if(!clases.containsKey(nombre)){
            clases.put(nombre,clase);
        }
        else{
            throw new SemanticException(clase.getToken(),"La clase "+nombre+" ya esta declarada");
        }
    }
    public void agregarInterfaz(String nombre,Interfaz interfaz) throws SemanticException {
        if(!interfaces.containsKey(nombre)){
            interfaces.put(nombre,interfaz);
        }
        else{
            throw new SemanticException(interfaz.getToken(),"La clase "+nombre+" ya esta declarada");
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


    public Metodo getMetodoActual() {
        return metodoActual;
    }

    public void setMetodoActual(Metodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    public Clase getClaseByName(String name){
        return clases.get(name);
    }
}
