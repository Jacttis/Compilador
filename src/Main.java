import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorLexico.Token;
import AnalizadorSintactico.SyntaxAnalyzer;
import AnalizadorSintactico.SyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        File file = null;
        FileManager fileManager = null;
        ArrayList<Exception> exceptions=new ArrayList<>();
        LexicalAnalyzer lexical;
        SyntaxAnalyzer syntaxAnalyzer;
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
            syntaxAnalyzer.inicial();
        } catch (LexicalException | IOException | SyntaxException e) {
            e.printStackTrace();

        }

        System.out.println("[SinErrores]");


    }
}
