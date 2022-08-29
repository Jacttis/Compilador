import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestFileManager {

    public static void main(String[] args) {
        File archivo=null;
         FileManager fileManager = null;
        try {
            if(args.length>0) {
                archivo = new File(args[0]);
            }
            else{
                System.out.println("No hay ningun archivo seleccionado");
                System.exit(0);
            }
            fileManager=new FileManager(archivo);
        } catch (FileNotFoundException e) {
            if(args[0]!=null) {
                System.out.println(args[0] + " archivo no encontrado");
            }
            System.exit(0);
        }
        LexicalAnalyzer lexical= null;
        try {
            lexical = new LexicalAnalyzer(fileManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Token ultimo=lexical.nextToken();
            System.out.println(ultimo.toString());
            while (!ultimo.getDescription().equals("EOF")) {
                ultimo=lexical.nextToken();
                System.out.println(ultimo.toString());
            }
            System.out.println("[SinErrores]");
        } catch (Exception e) {
            e.printStackTrace();
        }









        /*while(!fileManager.isEOF()){
            try {
                System.out.print(fileManager.getNextChar());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(fileManager.getLineNumber());*/
    }
}
