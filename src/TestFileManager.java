import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestFileManager {

    public static void main(String[] args) {
        File archivo=new File(args[0]);
         FileManager fileManager;
        try {
            fileManager=new FileManager(archivo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
