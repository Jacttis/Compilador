package AnalizadorSintactico;

import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorLexico.Token;

import java.io.IOException;

public class SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token actualToken;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException {
        this.lexicalAnalyzer=lexicalAnalyzer;
        actualToken= lexicalAnalyzer.nextToken();
    }

    private void match(String tokenDescription) throws SyntaxException, LexicalException, IOException {
        if (tokenDescription.equals(actualToken.getDescription())){
            actualToken = lexicalAnalyzer.nextToken();
        }
        else {
            throw new SyntaxException(actualToken,tokenDescription);
        }

    }
    public void inicial() throws LexicalException, SyntaxException, IOException {
        listaClases();
        match("EOF"); // PReguntar si se puede asi

    }

    private void listaClases() {
        if(actualToken.getDescription().equals("pr_class") ||actualToken.getDescription().equals("pr_interface") ){
            clase();
            listaClases();
        }
        else{
            
        }
            
    }

    private void clase() throws LexicalException, SyntaxException, IOException {
        if(actualToken.getDescription().equals("pr_class")){
            claseConcreta();
            
        } else if (actualToken.getDescription().equals("pr_interface") ) {
            interfaz();
        }
    }

    private void interfaz() {
    }

    private void claseConcreta() throws LexicalException, SyntaxException, IOException {
        match("pr_class");
        match("idClase");

    }
}