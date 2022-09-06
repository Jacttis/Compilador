package AnalizadorSintactico;

import AnalizadorLexico.Token;

public class SyntaxException extends Exception {
    private Token actualToken;
    private String expectedToken;

    public SyntaxException(Token actualToken, String expectedToken) {
        this.actualToken = actualToken;
        this.expectedToken = expectedToken;
    }

    public void printStackTrace(){
        System.out.println("Error Sintactico en linea: "+actualToken.getNumberline());
        System.out.println("Se esperaba "+expectedToken+" y se encontro\""+actualToken.getLexeme()+"\"");
        System.out.println("[Error:"+actualToken.getLexeme()+"|"+actualToken.getNumberline()+"]");
    }
}
