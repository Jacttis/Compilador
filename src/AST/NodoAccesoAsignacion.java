package AST;

import AST.Acceso.NodoAcceso;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
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
        if(acceso.esAsignable()){
            Tipo tipo=acceso.chequear();
            if(tipo.esSubtipo(expresion.chequear())){
                if(tipoOperador!=null){
                    if(!tipoOperador.compareTipo(tipo)){
                        //error
                    }
                }
            }
            else{
                //error
            }
        }
        else{
            //error
        }

    }
}
