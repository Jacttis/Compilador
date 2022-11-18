package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoThis extends NodoAcceso{

    protected Clase claseThis;
    protected MetodoConstructor metodo;
    public NodoAccesoThis(Token token, Clase clase, MetodoConstructor metodoConstructor) {
        super(token);
        claseThis=clase;
        metodo=metodoConstructor;
    }

    @Override
    public Tipo chequear() {
        if(metodo.isEstatico()){
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                    + accesoToken.getNumberline() + ": No se puede llamar a this en metodo estaticos " + accesoToken.getLexeme()));
            return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
        }
        if (nodoEncadenado==null){
            return new TipoReferencia(claseThis.getToken());
        }
        else {
            nodoEncadenado.setEsThis();
            Tipo tipo= nodoEncadenado.chequear(new TipoReferencia(claseThis.getToken()));
            if(tipo!=null){
                return tipo;
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(accesoToken, "Error Semantico en linea "
                        + accesoToken.getNumberline() + ": Tipo no puede ser primitivo " + accesoToken.getLexeme()));
                return new Tipo(new Token("pr_void","void",0));//Devuelvo para seguir ejecucion
            }
        }
    }

    @Override
    public void generarCodigo() {
        TablaDeSimbolos.codigoMaquina.add("LOAD 3 ");
        if(nodoEncadenado != null){
            if (ladoIzquierdo)
                nodoEncadenado.setLadoIzquierdo();
            nodoEncadenado.generarCodigo();
        }
    }
}
