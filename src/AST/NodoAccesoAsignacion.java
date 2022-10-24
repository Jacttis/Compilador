package AST;

import AST.Acceso.NodoAcceso;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

public class NodoAccesoAsignacion extends NodoSentencia{

    protected NodoAcceso acceso;
    protected Token asignacion;
    protected NodoExpresion expresion;


    public NodoAccesoAsignacion(NodoAcceso acceso,Token asignacion,NodoExpresion expresion){
        this.acceso=acceso;
        this.asignacion=asignacion;
        this.expresion=expresion;
    }
}
