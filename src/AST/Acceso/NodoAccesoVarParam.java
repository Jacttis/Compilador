package AST.Acceso;

import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoVarParam extends NodoAcceso {

    protected Clase clase;
    protected MetodoConstructor metodo;
    private NodoBloque bloque;
    public NodoAccesoVarParam(Token token, Clase clase, MetodoConstructor metodo, NodoBloque bloque) {
        super(token);
        esAsignable=true;
        this.clase=clase;
        this.metodo=metodo;
        this.bloque=bloque;
    }

    @Override
    public Tipo chequear() {
        Tipo tipo = null;
        if(clase.getAtributos().containsKey(accesoToken.getLexeme())) {
            tipo = clase.getAtributos().get(accesoToken.getLexeme()).getTipoAtributo();
        } else if (metodo.repiteNombre(accesoToken) ) {
            tipo = metodo.tipoParametro(accesoToken);
        } else if ( bloque.estaVarEnBloque(accesoToken)) {
            tipo = bloque.tipoVarEnBloque(accesoToken);
        }
        else {
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(accesoToken, "Error Semantico en linea "
                    + accesoToken.getNumberline() + ": ya existe un atributo con el mismo nombre " +accesoToken.getLexeme()));
        }

        if(nodoEncadenado==null){
            return tipo;
        }
        else{
            return nodoEncadenado.chequear((TipoReferencia) tipo);
        }

    }
}
