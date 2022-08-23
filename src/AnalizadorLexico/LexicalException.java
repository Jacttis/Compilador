package AnalizadorLexico;

public class LexicalException extends Exception{

    private String lexeme, actualLine,error;
    private int numberLine, columnActual;

    public LexicalException(String lexeme, int numberLine, String actualLine, int columnActual,String error){
        this.lexeme = lexeme;
        this.numberLine = numberLine;
        this.actualLine = actualLine;
        this.columnActual = columnActual;
        this.error=error;
    }

    public void printStackTrace(){
        System.out.println("Error Lexico en linea "+numberLine+" , columna "+ columnActual +" : "+ lexeme +" -> "+ error);
        System.out.println("Detalle : " + this.actualLine);
        for (int i = 0; i < columnErrorforMessage(); i++){
            System.out.print(" ");
        }
        System.out.println("^");
        System.out.println("[Error:"+ lexeme +"|"+ numberLine +"]");
    }

    private int columnErrorforMessage(){
        int letterInMessageDetalle=10;
        return columnActual-1+letterInMessageDetalle;
    }
}
