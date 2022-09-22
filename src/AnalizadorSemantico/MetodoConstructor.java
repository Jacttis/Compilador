package AnalizadorSemantico;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class MetodoConstructor {

    protected Hashtable<String,Parametro> tablaArgumentos;
    protected LinkedList<Parametro> listaArgumentos;

    public MetodoConstructor(){
        listaArgumentos = new LinkedList<Parametro>();
    }

    public abstract void setArgumento(String nombre, Parametro argumento) throws SemanticException;
    //public abstract void setVariable(EntradaVariable variable);   No de esta etapa var locales


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


    public LinkedList<Parametro> getListaArgumentos() {  return listaArgumentos;}
    public abstract void esta_bien_declarado() throws SemanticException;

}
