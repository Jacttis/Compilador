package AST.Acceso;

import AST.NodoBloque;
import AST.NodoVarLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoVarParam extends NodoAcceso {

    private static final int PARAMETRO = 1;
    private static final int VARLOCAL = 2;
    private static final int ATRIBUTO = 3;
    protected Clase clase;
    protected MetodoConstructor metodo;
    private NodoBloque bloque;
    private Object variable;
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
            variable =metodo.getParam(accesoToken);

        } else if ( bloque.estaVarEnBloque(accesoToken)) {

            tipo = bloque.tipoVarEnBloque(accesoToken);
            variable =bloque.getVarEnBloque(accesoToken);

        }else if(clase.getAtributos().containsKey(accesoToken.getLexeme())) {

            variable = clase.getAtributos().get(accesoToken.getLexeme());
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
    public void generarCodigo(){
        if(variable  instanceof Atributo){
            TablaDeSimbolos.codigoMaquina.add("LOAD 3 ;Apilo this");
            if(!ladoIzquierdo || nodoEncadenado!=null){
                TablaDeSimbolos.codigoMaquina.add("LOADREF"+((Atributo) variable).getOffset());
            }
            else{
                TablaDeSimbolos.codigoMaquina.add("SWAP");
                TablaDeSimbolos.codigoMaquina.add("STOREREF"+((Atributo) variable).getOffset());
            }
        }
        else if (variable instanceof Parametro){
            if(!ladoIzquierdo || nodoEncadenado!=null){
                TablaDeSimbolos.codigoMaquina.add("LOADREF"+ ((Parametro) variable).getOffset());
            }
            else{
                TablaDeSimbolos.codigoMaquina.add("STOREREF"+((Parametro) variable).getOffset());
            }
        }
        else if (variable instanceof NodoVarLocal){
            if(!ladoIzquierdo || nodoEncadenado!=null){
                TablaDeSimbolos.codigoMaquina.add("LOADREF"+ ((NodoVarLocal) variable).getOffset());
            }
            else{
                TablaDeSimbolos.codigoMaquina.add("STOREREF"+((NodoVarLocal) variable).getOffset());
            }
        }

        if(nodoEncadenado != null){
            if (ladoIzquierdo)
                nodoEncadenado.setLadoIzquierdo();
            nodoEncadenado.generarCodigo();

        }

    }
}
