package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

public class NodoVariableEncadenada extends NodoEncadenado {
    public NodoVariableEncadenada(Token tokenNodoEncadenado) {
        super(tokenNodoEncadenado);
    }
}
