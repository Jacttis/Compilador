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
        if (metodo.repiteNombre(accesoToken) ) {
            tipo = metodo.tipoParametro(accesoToken);
        } else if ( bloque.estaVarEnBloque(accesoToken)) {
            tipo = bloque.tipoVarEnBloque(accesoToken);
        }else if(clase.getAtributos().containsKey(accesoToken.getLexeme())) {
            if (metodo.isEstatico()){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": No se puede llamar a un atributo en metodo estaticos " + accesoToken.getLexeme()));
                return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
            }
            tipo = clase.getAtributos().get(accesoToken.getLexeme()).getTipoAtributo();
        } else {
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(accesoToken, "Error Semantico en linea "
                    + accesoToken.getNumberline() + ": No existe un atributo con el mismo nombre " +accesoToken.getLexeme()));
            return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
        }

        if(nodoEncadenado==null){
            return tipo;
        }
        else{
            Tipo tipoEncadenado= nodoEncadenado.chequear(tipo);
            if(tipoEncadenado!=null){
                return tipoEncadenado;
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
            }
        }

    }
}
