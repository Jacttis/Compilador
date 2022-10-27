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
    protected Clase claseActual;

    public NodoVarLocal(Token token, Tipo tipo, NodoBloque bloque, MetodoConstructor metodoConstructor,Clase clase){
        tokenVar=token;
        this.tipo=tipo;
        bloqueActual=bloque;
        metodoConstructorActual=metodoConstructor;
        claseActual=clase;
    }

    public Token getToken() {
        return tokenVar;
    }

    public void setTokenVar(Token tokenVar) {
        this.tokenVar = tokenVar;
    }

    public Tipo getTipo() {
        if(tipo==null){
            return expresion.chequear();
        }
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

    public void chequear() {
        if(bloqueActual.getBloqueContainer()!=null){
           if(bloqueActual.getBloqueContainer().estaVarEnBloque(tokenVar) || metodoConstructorActual.repiteNombre(tokenVar)){
               TablaDeSimbolos.listaExcepciones.add( new SemanticException(tokenVar, "Error Semantico en linea "
                       + tokenVar.getNumberline() + ": ya existe un atributo con el mismo nombre " +tokenVar.getLexeme()));
            }
        }
        else{
            if(metodoConstructorActual.repiteNombre(tokenVar)){
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(tokenVar, "Error Semantico en linea "
                        + tokenVar.getNumberline() + ": ya existe un atributo con el mismo nombre " +tokenVar.getLexeme()));
            }
        }
        if(tipo!=null) {
            tipo.checkTipo(claseActual);
            if (expresion != null) {
                if (!tipo.esSubtipo(expresion.chequear(),this)) {
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenVar, "Error Semantico en linea "
                            + tokenVar.getNumberline() + ": tipo de variable y expresion distintos " + tokenVar.getLexeme()));
                }
            }
        }
        else{
            Tipo tipoExpresion=expresion.chequear();
            if(tipoExpresion.compareTipo(new Tipo(new Token("pr_null","null",0))) || tipoExpresion.compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(tokenVar, "Error Semantico en linea "
                        + tokenVar.getNumberline() + ": tipo no puede null ni void " + tokenVar.getLexeme()));

            }
            else{
                tipo=tipoExpresion;
            }

        }
    }
}
