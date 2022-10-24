package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoAccesoMetodoEstatico extends NodoAcceso {
    Token tokenClase;

    LinkedList<NodoExpresion> parametros;
    public NodoAccesoMetodoEstatico(Token token,Token tokenClase) {
        super(token);
        this.tokenClase=tokenClase;
    }

    public LinkedList<NodoExpresion> getParametros() {
        return parametros;
    }

    public void setParametros(LinkedList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }
}
