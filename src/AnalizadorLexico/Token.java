package AnalizadorLexico;

public class Token {
    protected String description;
    protected String lexeme;
    protected int numberline;
    public Token(String description, String lexeme, int lineNumber) {
        this.description =description;
        this.lexeme=lexeme;
        this.numberline=lineNumber;
    }

    @Override
    public String toString() {
        return "("+description+","+lexeme+","+numberline+")";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getNumberline() {
        return numberline;
    }

    public void setNumberline(int numberline) {
        this.numberline = numberline;
    }
}
