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

            Token ultimo2=lexical.nextToken();
            System.out.println(ultimo2.toString());

            Token ultimo3=lexical.nextToken();
            System.out.println(ultimo3.toString());

            Token ultimo4=lexical.nextToken();
            System.out.println(ultimo4.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
