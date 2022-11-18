package AST;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoReturn extends NodoSentencia{

    protected NodoExpresion expresion;
    protected Metodo metodoActual;
    protected Token token;
    protected NodoBloque bloque;

    public NodoReturn(Token token, NodoExpresion expresion, Metodo metodo,NodoBloque bloqueActual){
        this.expresion=expresion;
        metodoActual=metodo;
        this.token=token;
        bloque=bloqueActual;
    }

    @Override
    public Token getToken() {
        return token;
    }

    public void chequear(){
        if(expresion==null){
           if(!metodoActual.getTipo().getToken().getLexeme().equals("void")){
               TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                       + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
           }
        }
        else {
            Tipo tipoExpresion=expresion.chequear();
            if(tipoExpresion.compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": tipo de expresion no puede ser void " + token.getLexeme()));
            }else{
                if (!tipoExpresion.compareTipo(new Tipo(new Token("pr_null","null",0)))){
                    if(!metodoActual.getTipo().esSubtipo(tipoExpresion,this)){
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
                    }
                }
                else {
                    if(!(metodoActual.getTipo() instanceof TipoReferencia)){
                        TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                                + token.getNumberline() + ": tipo de retorno y expresion distintos " + token.getLexeme()));
                    }
                }

            }
        }


    }

    @Override
    public void generarCodigo() {

        TablaDeSimbolos.codigoMaquina.add("FMEM "+bloque.obtenerSizeVarLocal() + " ; Se libera memoria de variables locales ");
        if(metodoActual.getTipo().compareTipo(new Tipo(new Token("pr_void","void",0)))){
            TablaDeSimbolos.codigoMaquina.add("STOREFP ; actualizo el FP para que apunte al RA llamador");
            if (metodoActual.isEstatico()){
                TablaDeSimbolos.codigoMaquina.add("RET "+metodoActual.getListaArgumentos().size());
            }
            else {
                TablaDeSimbolos.codigoMaquina.add("RET "+(metodoActual.getListaArgumentos().size()+1));
            }
        } else{
            expresion.generarCodigo();
            if (metodoActual.isEstatico()){
                TablaDeSimbolos.codigoMaquina.add("STORE "+ (metodoActual.getListaArgumentos().size()+3));
                TablaDeSimbolos.codigoMaquina.add("STOREFP ; actualizo el FP para que apunte al RA llamador");
                TablaDeSimbolos.codigoMaquina.add("RET "+ metodoActual.getListaArgumentos().size()+ " ; Se liberan " + metodoActual.getListaArgumentos().size() + " lugares de la pila");
            }
            else {
                TablaDeSimbolos.codigoMaquina.add("STORE "+ (metodoActual.getListaArgumentos().size()+4));//this
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET "+ (metodoActual.getListaArgumentos().size()+1)+ " ; Se liberan " + (metodoActual.getListaArgumentos().size()+1) + " lugares de la pila");
            }

        }
    }
}
