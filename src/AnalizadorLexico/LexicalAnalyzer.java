package AnalizadorLexico;

import java.io.IOException;
import java.util.HashMap;

public class LexicalAnalyzer {
    protected String lexeme;
    protected char actualChar;
    protected FileManager fileManager;
    protected HashMap<String,Token> reservedKeywords;

    protected int commentLineNumber =1, commentColumnNumber =0;
    protected String firstLineComment =null, lexemeComment =null;

    private boolean e0ThrowExcepcion =false;
    
    public LexicalAnalyzer(FileManager fileManager) throws IOException {
        this.fileManager=fileManager;
        actualChar=fileManager.getNextChar();
        reservedKeywords=new HashMap<>();
        fillhashMap();
    }
    public Token nextToken() throws LexicalException, IOException {
        if(e0ThrowExcepcion){
            actualChar=fileManager.getNextChar();
            e0ThrowExcepcion =false;
        }
        lexeme ="";
        return e0();
    }


    private Token e0() throws LexicalException, IOException {
        if (fileManager.isEOF()){
            return e5();
        } else if(Character.isDigit(actualChar)){
            refreshlexeme();
            refreshActualCharacter();
            return e1(1);
        } else if (Character.isUpperCase(actualChar)) {
            refreshlexeme();
            refreshActualCharacter();
            return e2();
        } else if (Character.isLowerCase(actualChar)) {
            refreshlexeme();
            refreshActualCharacter();
            return e18();
        } else if (actualChar == '>') {
            refreshlexeme();
            refreshActualCharacter();
            return e3();
        } else if (Character.isWhitespace(actualChar) || actualChar=='\n'|| actualChar=='\r') {
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
        } else if (actualChar==('\'')) {
            refreshlexeme();
            refreshActualCharacter();
            return e10();
        } else if (actualChar==('/')) {
            refreshlexeme();
            refreshActualCharacter();
            commentLineNumber =fileManager.getLineNumber();
            commentColumnNumber =fileManager.getActualColumnNumber()-1;
            return e14();
        } else if (actualChar=='"') {
            refreshlexeme();
            refreshActualCharacter();
            return e19();
        } else if (actualChar=='=') {
            refreshlexeme();
            refreshActualCharacter();
            return e22();
        } else if (actualChar=='+') {
            refreshlexeme();
            refreshActualCharacter();
            return e24();
        } else if (actualChar=='-') {
            refreshlexeme();
            refreshActualCharacter();
            return e26();
        } else if (actualChar=='*') {
            refreshlexeme();
            refreshActualCharacter();
            return e28();
        } else if (actualChar=='&') {
            refreshlexeme();
            refreshActualCharacter();
            return e29();
        } else if (actualChar=='|') {
            refreshlexeme();
            refreshActualCharacter();
            return e30();
        }else if (actualChar=='%') {
            refreshlexeme();
            refreshActualCharacter();
            return e31();
        } else if (actualChar=='(') {
            refreshlexeme();
            refreshActualCharacter();
            return e32();
        }else if (actualChar==')') {
            refreshlexeme();
            refreshActualCharacter();
            return e33();
        }else if (actualChar=='{') {
            refreshlexeme();
            refreshActualCharacter();
            return e34();
        }else if (actualChar=='}') {
            refreshlexeme();
            refreshActualCharacter();
            return e35();
        }else if (actualChar==';') {
            refreshlexeme();
            refreshActualCharacter();
            return e36();
        }else if (actualChar==',') {
            refreshlexeme();
            refreshActualCharacter();
            return e37();
        }else if (actualChar=='.') {
            refreshlexeme();
            refreshActualCharacter();
            return e38();
        }else {
            refreshlexeme();
            e0ThrowExcepcion =true;
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Caracter no valido");
        }
    }



    private Token e1(int numberOfDigits) throws IOException, LexicalException {
        if (Character.isDigit(actualChar) && numberOfDigits<9){
            refreshlexeme();
            refreshActualCharacter();
            return e1(numberOfDigits+1);
        } else if (Character.isDigit(actualChar )&& numberOfDigits>=9) {
            refreshlexeme();
            refreshActualCharacter();
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber()-1,"Entero de mas de 9 digitos");
        } else {
            return new Token("intLiteral",lexeme,fileManager.getLineNumber());
        }
    }
    private Token e2() throws IOException {
        if (Character.isLetterOrDigit(actualChar) || actualChar=='_'){
            refreshlexeme();
            refreshActualCharacter();
            return e2();
        }
        else {
            return new Token("idClase",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e3() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e4();
        }
        else {
            return new Token(">",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e4() {
        return new Token(">=",lexeme,fileManager.getLineNumber());
    }

    private Token e5() {
        return new Token("EOF","", fileManager.getLineNumber());
    }

    private Token e6() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e7();
        }
        else {
            return new Token("<",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e7() {
        return new Token("<=",lexeme,fileManager.getLineNumber());
    }
    private Token e8() throws IOException {
        if (actualChar == '='){
            refreshlexeme();
            refreshActualCharacter();
            return e9();
        }
        else {
            return new Token("!",lexeme,fileManager.getLineNumber());
        }
    }

    private Token e9() {
        return new Token("!=",lexeme,fileManager.getLineNumber());
    }

    private Token e10() throws IOException, LexicalException {
        if (actualChar!='\\'&& actualChar!='\''&& actualChar!='\n' && actualChar!='\r'){
            refreshlexeme();
            refreshActualCharacter();
            return e11();
        } else if (actualChar=='\\') {
            refreshlexeme();
            refreshActualCharacter();
            return e13();
        }  else if (actualChar=='\n' || actualChar=='\r') {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"No se permite Salto de linea en un Character ");
        }else {
            refreshlexeme();
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"LiteralChar vacio no valido");
        }
    }
    private Token e11() throws IOException, LexicalException {
        if(actualChar=='\'') {
            refreshlexeme();
            refreshActualCharacter();
            return e12();
        }
        else {
            if (!fileManager.EOF && actualChar!='\n' && actualChar!='\r'){
                refreshlexeme();
            }
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"No es un caracter Valido");

        }
    }

    private Token e12() {
        return new Token("charLiteral",lexeme, fileManager.getLineNumber());
    }

    private Token e39(int numbersInHexa) throws IOException, LexicalException {
        if ((actualChar>='0' && actualChar<='9' || actualChar>='a' && actualChar<='f' || actualChar>='A' && actualChar<='F') && numbersInHexa<=4){
            refreshlexeme();
            refreshActualCharacter();
            return e39(numbersInHexa+1);
        } else if ((actualChar>='0' && actualChar<='9' || actualChar>='a' && actualChar<='f' || actualChar>='A' && actualChar<='F') && numbersInHexa>4){
            refreshlexeme();
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Char Unicode de mas de 4 digitos hexa");
        } else if (actualChar=='\'' && numbersInHexa==5) {
            refreshlexeme();
            refreshActualCharacter();
            return e12();
        } else{
            if (!fileManager.EOF && actualChar!='\n' && actualChar!='\r'){
                refreshlexeme();
            }
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Char Unicode incorrecto");
        }
    }

    private Token e13() throws IOException, LexicalException {
        if(actualChar=='u') {
            refreshlexeme();
            refreshActualCharacter();
            return e39(1);
        }
        else {
            refreshlexeme();
            refreshActualCharacter();
            return e11();
        }

    }
    private Token e14() throws IOException, LexicalException {
        if (actualChar==('/')) {
            deleteLexeme();
            refreshActualCharacter();
            return e15();
        }
        else if (actualChar==('*')){
            refreshlexeme();
            refreshActualCharacter();
            return e16();
        }
        else{
            return new Token("/",lexeme, fileManager.getLineNumber());
        }
    }
    private Token e15() throws IOException, LexicalException {
        if(actualChar =='\n'){
            refreshActualCharacter();
            return e0();
        }
        else {
            refreshActualCharacter();
            return e15();
        }
    }


    private Token e16() throws IOException, LexicalException {
        if(actualChar=='*'){
            refreshActualCharacter();
            return e17();
        } else if (!fileManager.isEOF()) {
            if(actualChar!='\r'&& actualChar!='\n' ) {
                refreshlexeme();
            }
            if(actualChar=='\r' && firstLineComment ==null){
                firstLineComment =""+fileManager.getActualLine();
                lexemeComment =lexeme;
            }
            refreshActualCharacter();
            return e16();
        }
        else {
            if(firstLineComment ==null){
                firstLineComment =""+fileManager.getActualLine();
                lexemeComment =lexeme;
            }
            throw new LexicalException(lexemeComment, commentLineNumber, firstLineComment, commentColumnNumber,"Comentario multilinea sin cerrar se llego al final del archivo");
        }
    }

    private Token e17() throws IOException, LexicalException {
        if(actualChar=='/'){
            refreshActualCharacter();
            firstLineComment =null;
            commentLineNumber =1;
            commentColumnNumber =0;
            deleteLexeme();
            return e0();
        }
        else {
            refreshActualCharacter();
            return e16();
        }
    }
    private Token e18() throws IOException {
        if (Character.isLetterOrDigit(actualChar) || actualChar=='_'){
            refreshlexeme();
            refreshActualCharacter();
            return e18();
        } else if (reservedKeywords.containsKey(lexeme)) {
            Token tokenReserved=reservedKeywords.get(lexeme);
            Token newToken=new Token(tokenReserved.getDescription(),tokenReserved.getLexeme(), fileManager.getLineNumber());
            return newToken;
        } else {
            return new Token("idMetVar",lexeme,fileManager.getLineNumber());
        }
    }
    private Token e19() throws LexicalException, IOException {
        if(!fileManager.EOF) {
            if (actualChar=='\\'){
                refreshlexeme();
                refreshActualCharacter();
                return e20();
            } else if (actualChar=='"') {
                refreshlexeme();
                refreshActualCharacter();
                return e21();
            } else if (actualChar!='\n'&& actualChar!='\r') {
                refreshlexeme();
                refreshActualCharacter();
                return e19();
            }
            else {
                throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"No se permite Salto de linea en String ");
            }
        }
        else {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Se llego al EOF String sin cerrar");
        }
    }

    private Token e21() {
        return new Token("stringLiteral",lexeme,fileManager.getLineNumber());
    }

    private Token e20() throws LexicalException, IOException {
        if (actualChar!='\n'&& actualChar!='\r') {
            refreshlexeme();
            refreshActualCharacter();
            return e19();
        }
        else {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"No se permite Salto de linea en String ");
        }
    }

    private Token e22() throws IOException {
        if(actualChar=='='){
            refreshlexeme();
            refreshActualCharacter();
            return e23();
        }
        else {
            return new Token("=",lexeme, fileManager.getLineNumber());
        }
    }

    private Token e23() {
        return new Token("==",lexeme, fileManager.getLineNumber());
    }
    private Token e24() throws IOException {
        if(actualChar=='='){
            refreshlexeme();
            refreshActualCharacter();
            return e25();
        }
        else {
            return new Token("+",lexeme, fileManager.getLineNumber());
        }
    }

    private Token e25() {
        return new Token("+=",lexeme, fileManager.getLineNumber());
    }

    private Token e26() throws IOException {
        if(actualChar=='='){
            refreshlexeme();
            refreshActualCharacter();
            return e27();
        }
        else {
            return new Token("-",lexeme, fileManager.getLineNumber());
        }
    }

    private Token e27() {
        return new Token("-=",lexeme, fileManager.getLineNumber());
    }

    private Token e28() {
        return new Token("*",lexeme,fileManager.getLineNumber());
    }
    private Token e29() throws LexicalException, IOException {
        if (actualChar=='&') {
            refreshlexeme();
            refreshActualCharacter();
            return new Token("&&",lexeme,fileManager.getLineNumber());
        }
        else {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Se esperaba otro &");
        }
    }

    private Token e30() throws LexicalException, IOException {
        if (actualChar=='|') {
            refreshlexeme();
            refreshActualCharacter();
            return new Token("||",lexeme,fileManager.getLineNumber());
        }
        else {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getActualLine(), fileManager.getActualColumnNumber(),"Se esperaba otro |");
        }
    }

    private Token e31() {
        return new Token("%",lexeme,fileManager.getLineNumber());
    }

    private Token e32() {
        return new Token("abreParentesis",lexeme,fileManager.getLineNumber());
    }
    private Token e33() {
        return new Token("cierraParentesis",lexeme,fileManager.getLineNumber());
    }
    private Token e34() {
        return new Token("abreCorchete",lexeme,fileManager.getLineNumber());
    }
    private Token e35() {
        return new Token("cierraCorchete",lexeme,fileManager.getLineNumber());
    }
    private Token e36() {
        return new Token("puntoComa",lexeme,fileManager.getLineNumber());
    }
    private Token e37() {
        return new Token("coma",lexeme,fileManager.getLineNumber());
    }
    private Token e38() {
        return new Token("punto",lexeme,fileManager.getLineNumber());
    }



    private void refreshlexeme(){
        lexeme = lexeme + actualChar;
    }
    private void refreshActualCharacter() throws IOException {
        actualChar = fileManager.getNextChar();
    }

    private void deleteLexeme(){
        lexeme="";
    }

    private void fillhashMap(){
        reservedKeywords.put("class",new Token("pr_class","class",0));
        reservedKeywords.put("interface",new Token("pr_interface","interface",0));
        reservedKeywords.put("extends",new Token("pr_extends","extends",0));
        reservedKeywords.put("implements",new Token("pr_implements","implements",0));
        reservedKeywords.put("public",new Token("pr_public","public",0));
        reservedKeywords.put("private",new Token("pr_private","private",0));
        reservedKeywords.put("static",new Token("pr_static","static",0));
        reservedKeywords.put("void",new Token("pr_void","void",0));
        reservedKeywords.put("boolean",new Token("pr_boolean","boolean",0));
        reservedKeywords.put("char",new Token("pr_char","char",0));
        reservedKeywords.put("int",new Token("pr_int","int",0));
        reservedKeywords.put("if",new Token("pr_if","if",0));
        reservedKeywords.put("else",new Token("pr_else","else",0));
        reservedKeywords.put("while",new Token("pr_while","while",0));
        reservedKeywords.put("return",new Token("pr_return","return",0));
        reservedKeywords.put("var",new Token("pr_var","var",0));
        reservedKeywords.put("this",new Token("pr_this","this",0));
        reservedKeywords.put("new",new Token("pr_new","new",0));
        reservedKeywords.put("null",new Token("pr_null","null",0));
        reservedKeywords.put("true",new Token("pr_true","true",0));
        reservedKeywords.put("false",new Token("pr_false","false",0));
    }

}
