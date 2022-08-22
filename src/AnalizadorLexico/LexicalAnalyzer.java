package AnalizadorLexico;

import java.io.IOException;

public class LexicalAnalyzer {
    String lexeme;
    char actualChar;
    FileManager fileManager;
    
    public LexicalAnalyzer(FileManager fileManager) throws IOException {
        this.fileManager=fileManager;
        actualChar=fileManager.getNextChar();
    }
    public Token nextToken() throws Exception {
        lexeme ="";
        return e0();
    }

    private Token e0() throws Exception {
        if (fileManager.isEOF()){
            return e5();
        } else if(Character.isDigit(actualChar)){
            refreshlexeme();
            refreshActualCharacter();
            return e1();
        } else if (Character.isLetter(actualChar)) {
            refreshlexeme();
            refreshActualCharacter();
            return e2();
        } else if (actualChar == '>') {
            refreshlexeme();
            refreshActualCharacter();
            return e3();
        } else if (Character.isWhitespace(actualChar) || actualChar=='\n') {
            refreshActualCharacter();
            return e0();
        } else if (actualChar=='<') {
            refreshlexeme();
            refreshActualCharacter();
            return e6();
        } else if (actualChar=='!') {
            refreshlexeme();
            refreshActualCharacter();
            return e8();
        } else {
            refreshlexeme();
            throw new Exception(lexeme+"linea"+fileManager.getLineNumber());
        }
    }

    private Token e1() throws IOException {
        if (Character.isDigit(actualChar)){
            refreshlexeme();
            refreshActualCharacter();
            return e1();
        }else {
            return new Token("entero",lexeme,fileManager.getLineNumber());
        }
    }
    private Token e2() throws IOException {
        if (Character.isLetterOrDigit(actualChar)){
            refreshlexeme();
            refreshActualCharacter();
            return e2();
        }
        else {
            return new Token("identificador",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e3() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e4();
        }
        else {
            return new Token("Mayor",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e4() {
        return new Token("MayorIgual",lexeme,fileManager.getLineNumber());
    }

    private Token e5() {
        return new Token("EOF",lexeme, fileManager.getLineNumber());
    }

    private Token e6() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e7();
        }
        else {
            return new Token("Menor",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e7() {
        return new Token("MenorIgual",lexeme,fileManager.getLineNumber());
    }
    private Token e8() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e9();
        }
        else {
            return new Token("Negado",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e9() {
        return new Token("Distinto",lexeme,fileManager.getLineNumber());
    }
    private void refreshlexeme(){
        lexeme = lexeme + actualChar;
        System.out.println("lexema: "+lexeme);
    }
    private void refreshActualCharacter() throws IOException {
        actualChar = fileManager.getNextChar();
    }

}
