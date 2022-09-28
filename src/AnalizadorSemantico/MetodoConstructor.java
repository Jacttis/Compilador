package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class MetodoConstructor {

    protected LinkedList<Parametro> listaArgumentos;
    protected Token token;


    public MetodoConstructor(){
        listaArgumentos = new LinkedList<Parametro>();
    }

    public abstract void addArgumento(Parametro argumento) throws SemanticException;

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


}
