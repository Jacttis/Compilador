package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Clase;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

public class NodoAccesoThis extends NodoAcceso{

    protected Clase claseThis;
    public NodoAccesoThis(Token token, Clase clase) {
        super(token);
        claseThis=clase;
    }

    @Override
    public Tipo chequear() {
        return new TipoReferencia(claseThis.getToken());
    }
}
