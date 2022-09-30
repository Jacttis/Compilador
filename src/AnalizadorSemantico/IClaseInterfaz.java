package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;
import java.util.LinkedList;

public interface IClaseInterfaz {

    public void agregarMetodo(String nombreMetodo, Metodo metodo) throws SemanticException;

    public void agregarParametro(Token parametro) throws SemanticException;
    public void agregarInterfaz(Token token,LinkedList<Token> interfaz) throws SemanticException;
    public Hashtable<String, Token> getParametrosGenericos();


}
