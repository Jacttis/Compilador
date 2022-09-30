import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorLexico.Token;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSintactico.SyntaxAnalyzer;
import AnalizadorSintactico.SyntaxAnalyzerEtapa2;
import AnalizadorSintactico.SyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    static LinkedList<SemanticException> excepciones=new LinkedList<>();
    public static void main(String[] args) {
        File file = null;
        FileManager fileManager = null;
        LexicalAnalyzer lexical;
        SyntaxAnalyzer syntaxAnalyzer;

        boolean errores=false;
        try {
            if(args.length>0) {
                file = new File(args[0]);
                fileManager=new FileManager(file);
            }
            else{
                System.out.println("No hay ningun archivo seleccionado");
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            if(args[0]!=null) {
                System.out.println(args[0] + " archivo no encontrado");
            }
            System.exit(0);
        }

        try {
            lexical = new LexicalAnalyzer(fileManager);
            syntaxAnalyzer= new SyntaxAnalyzer(lexical);
            TablaDeSimbolos tablaDeSimbolo=new TablaDeSimbolos();
            syntaxAnalyzer.inicial();
            tablaDeSimbolo.checkDeclaracion();
            tablaDeSimbolo.consolidar();
        } catch (LexicalException | IOException | SyntaxException | SemanticException e) {
            e.printStackTrace();
            errores=true;

        }
        if (!errores)
            System.out.println("[SinErrores]");


    }
}
