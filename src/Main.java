import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.Token;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Token lastTokenReceived = null;
        while(lastTokenReceived==null) {
            try {
                lastTokenReceived = lexical.nextToken();
                System.out.println(lastTokenReceived.toString());
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
            while (!lastTokenReceived.getDescription().equals("EOF")) {
                try {
                    lastTokenReceived = lexical.nextToken();
                    System.out.println(lastTokenReceived.toString());
                }
                catch (Exception e){
                    exceptions.add(e);
                }
            }
            for (Exception erroresLexicos:exceptions) {
                erroresLexicos.printStackTrace();
            }
            if(exceptions.isEmpty()){
                System.out.println("[SinErrores]");
            }

    }
}
