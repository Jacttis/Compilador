package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSemantico.Tipo;

public class NodoOperandoLiteral extends NodoOperando{

    public static int stringN=1;
    public NodoOperandoLiteral(Token operando,Tipo tipo) {
        super(operando);
        this.tipo=tipo;
    }

    public Tipo chequear(){
        return tipo;
    }

    @Override
    public void generarCodigo() {
        if (tokenOperando.getLexeme().equals("false") || tipo.getToken().getLexeme().equals("null")){
            TablaDeSimbolos.codigoMaquina.add("PUSH 0");
        } else if (tokenOperando.getLexeme().equals("true")) {
            TablaDeSimbolos.codigoMaquina.add("PUSH 1");
        } else if (tokenOperando.getDescription().equals("intLiteral") || tokenOperando.getDescription().equals("charLiteral")) {
            TablaDeSimbolos.codigoMaquina.add("PUSH "+tokenOperando.getLexeme());
        } else if (tipo.getToken().getLexeme().equals("String")) {
            TablaDeSimbolos.codigoMaquina.add(".DATA");
            String etiqueta="lString"+(stringN++);
            TablaDeSimbolos.codigoMaquina.add(etiqueta+":");
            TablaDeSimbolos.codigoMaquina.add("DW "+tokenOperando.getLexeme()+ ", 0");
            TablaDeSimbolos.codigoMaquina.add(".CODE");
            TablaDeSimbolos.codigoMaquina.add("PUSH "+etiqueta);

        }
    }
}
