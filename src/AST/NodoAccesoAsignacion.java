package AST;

import AST.Acceso.NodoAcceso;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

import java.util.Arrays;

public class NodoAccesoAsignacion extends NodoSentencia{

    protected NodoAcceso acceso;
    protected Token asignacion;
    protected NodoExpresion expresion;
    protected Tipo tipoOperador;

    protected Tipo tipoAcceso;

    @Override
    public Token getToken() {
        return asignacion;
    }

    public NodoAccesoAsignacion(NodoAcceso acceso, Token asignacion, NodoExpresion expresion){
        this.acceso=acceso;
        this.asignacion=asignacion;
        this.expresion=expresion;
        if(asignacion!=null) {
            if (Arrays.asList("+=", "-=").contains(asignacion.getDescription())) {
                tipoOperador = new TipoPrimitivo(new Token("pr_int", "int", 0));
            }
        }
    }

    @Override
    public void chequear() {
        if (expresion != null) {
            if(acceso.esAsignable()){
                Tipo tipo=acceso.chequear();
                tipoAcceso=tipo;
                Tipo tipoExpresion=expresion.chequear();


                if(tipoExpresion.compareTipo(new Tipo(new Token("pr_null","null",0))) && tipo.getToken().getDescription().equals("idClase")){
                    return;
                }
                if(expresion.chequear().compareTipo(new Tipo(new Token("pr_void","void",0)))){
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                            + asignacion.getNumberline() + ": tipo de expresion no puede ser void " + asignacion.getLexeme()));
                    return;
                }

                if(tipo.esSubtipo(tipoExpresion,this)){
                    if(tipoOperador!=null){
                        if(!tipoOperador.compareTipo(tipo)){
                            TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                                    + asignacion.getNumberline() + ": tipo de variable y expresion distintos " + asignacion.getLexeme()));
                        }
                    }
                }
                else{
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                            + asignacion.getNumberline() + ": tipo de variable y expresion distintos " + asignacion.getLexeme()));
                }
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                        + asignacion.getNumberline() + ": Lado derecho no es asignable " + asignacion.getLexeme()));
            }
        }
        else{
            //Llamada
            Object[] llamable=acceso.isLLamable();
            if ((Boolean) llamable[0]){
                tipoAcceso=acceso.chequear();
            }
            else{
                Token token= (Token) llamable[1];
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(token, "Error Semantico en linea "
                        + token.getNumberline() + ": No es llamable " + token.getLexeme()));
                acceso.chequear();//?Chequeo para ver otro error
            }
        }
    }

    @Override
    public void generarCodigo() {
        if(expresion!=null){
            if (asignacion.getLexeme().equals("+=")){
                acceso.generarCodigo();
                expresion.generarCodigo();
                TablaDeSimbolos.codigoMaquina.add("ADD ; realizo la suma");
                acceso.setLadoIzquierdo();
                acceso.generarCodigo();
            }
            else if (asignacion.getLexeme().equals("-=")){
                acceso.generarCodigo();
                expresion.generarCodigo();
                TablaDeSimbolos.codigoMaquina.add("SUB ; realizo la resta");
                acceso.setLadoIzquierdo();
                acceso.generarCodigo();
            }else {
                expresion.generarCodigo();
                acceso.setLadoIzquierdo();
                acceso.generarCodigo();
            }
        }else{
            acceso.setLadoIzquierdo();
            acceso.generarCodigo();
            if(!tipoAcceso.compareTipo(new Tipo(new Token("pr_void","void",0)))){
                TablaDeSimbolos.codigoMaquina.add("POP");
            }
        }
    }
}
