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


    public NodoAccesoAsignacion(NodoAcceso acceso,Token asignacion,NodoExpresion expresion){
        this.acceso=acceso;
        this.asignacion=asignacion;
        this.expresion=expresion;
        if(Arrays.asList("+=","-=").contains(asignacion.getDescription())){
            tipoOperador=new TipoPrimitivo(new Token("pr_int","int",0));
        }
    }

    @Override
    public void chequear() {
        if (expresion != null) {
            if(acceso.esAsignable()){
                Tipo tipo=acceso.chequear();
                Tipo tipoExpresion=expresion.chequear();

                if(expresion.chequear().compareTipo(new Tipo(new Token("pr_void","void",0)))){
                    TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                            + asignacion.getNumberline() + ": tipo de expresion no puede ser void " + asignacion.getLexeme()));
                    return;
                }

                if(tipo.esSubtipo(tipoExpresion)){
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
            if (acceso.isLLamable()){
                acceso.chequear();
            }
            else{
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(asignacion, "Error Semantico en linea "
                        + asignacion.getNumberline() + ": No es llamable " + asignacion.getLexeme()));

            }
        }


    }
}
