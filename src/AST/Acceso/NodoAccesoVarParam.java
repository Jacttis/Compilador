package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoVarParam extends NodoAcceso {

    public NodoAccesoVarParam(Token token) {
        super(token);
        esAsignable=true;
    }

}
