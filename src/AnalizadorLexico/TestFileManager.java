package AnalizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestFileManager {

    public static void main(String[] args) throws IOException {
        File archivo=new File("D:\\Compilador\\Compilador\\src\\AnalizadorLexico\\test.txt");
         FileManager fileManager;
        try {
            fileManager=new FileManager(archivo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        LexicalAnalyzer lexical=new LexicalAnalyzer(fileManager);
        try {
            Token ultimo=lexical.nextToken();
            System.out.println(ultimo.toString());
            while (!ultimo.description.equals("EOF")) {
                ultimo=lexical.nextToken();
                System.out.println(ultimo.toString());
            }
            System.out.println("[Sin Errores]");
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
