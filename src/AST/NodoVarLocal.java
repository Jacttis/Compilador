package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoVarLocal extends NodoSentencia{

    protected Token tokenVar;
    protected Tipo tipo;
    protected NodoBloque bloqueActual;
    protected MetodoConstructor metodoConstructorActual;

    protected NodoExpresion expresion;

    public NodoVarLocal(Token token, Tipo tipo, NodoBloque bloque, MetodoConstructor metodoConstructor){
        tokenVar=token;
        this.tipo=tipo;
        bloqueActual=bloque;
        metodoConstructorActual=metodoConstructor;
    }

    public Token getTokenVar() {
        return tokenVar;
    }

    public void setTokenVar(Token tokenVar) {
        this.tokenVar = tokenVar;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public NodoBloque getBloqueActual() {
        return bloqueActual;
    }

    public void setBloqueActual(NodoBloque bloqueActual) {
        this.bloqueActual = bloqueActual;
    }

    public MetodoConstructor getMetodoConstructorActual() {
        return metodoConstructorActual;
    }

    public void setMetodoConstructorActual(MetodoConstructor metodoConstructorActual) {
        this.metodoConstructorActual = metodoConstructorActual;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    protected boolean esVar() {
        return true;
    }

    public void chequear(IClaseInterfaz claseActual) {
        tipo.checkTipo(claseActual);
        if(expresion!=null){
            if(!tipo.esSubtipo(expresion.chequear())){
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(tokenVar, "Error Semantico en linea "
                        + tokenVar.getNumberline() + ": tipo de variable y expresion distintos " +tokenVar.getLexeme()));
            }
        }

    }
}
