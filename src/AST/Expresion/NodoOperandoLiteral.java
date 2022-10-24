package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;

public class NodoOperandoLiteral extends NodoOperando{


    public NodoOperandoLiteral(Token operando,Tipo tipo) {
        super(operando);
        this.tipo=tipo;
    }

    public Tipo chequear(){
        return tipo;
    }
}
