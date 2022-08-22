package AnalizadorLexico;

public class Token {
    String description;
    String lexeme;
    int numberline;
    public Token(String description, String lexeme, int lineNumber) {
        this.description =description;
        this.lexeme=lexeme;
        this.numberline=lineNumber;
    }

    @Override
    public String toString() {
        return "description "+description+" numero de linea "+numberline;
    }
}
