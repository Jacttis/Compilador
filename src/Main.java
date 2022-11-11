import AnalizadorLexico.FileManager;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorSemantico.SemanticException;
import AnalizadorSemantico.TablaDeSimbolos;
import AnalizadorSintactico.SyntaxAnalyzer;
import AnalizadorSintactico.SyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
            String outputFile=args[1];
            lexical = new LexicalAnalyzer(fileManager);
            syntaxAnalyzer= new SyntaxAnalyzer(lexical);
            TablaDeSimbolos tablaDeSimbolo=new TablaDeSimbolos();
            syntaxAnalyzer.inicial();
            tablaDeSimbolo.checkDeclaracion();
            tablaDeSimbolo.consolidar();
            tablaDeSimbolo.chequearBloques();
            TablaDeSimbolos.tablaSimbolos.generarCodigo();
            crearArchivo(outputFile,TablaDeSimbolos.codigoMaquina);
            for (Exception e:TablaDeSimbolos.listaExcepciones) {
                e.printStackTrace();
            }
        } catch (LexicalException | IOException | SyntaxException | SemanticException e) {
            e.printStackTrace();
            errores=true;

        }
        if (!errores && TablaDeSimbolos.listaExcepciones.isEmpty())
            System.out.println("[SinErrores]");

        TablaDeSimbolos.listaExcepciones.clear();
        TablaDeSimbolos.codigoMaquina.clear();

    }

    public static void crearArchivo(String archivo,LinkedList<String> codigo) throws IOException {
        FileWriter writer=new FileWriter(archivo);
        for (String instruccion:codigo) {
            writer.write(instruccion+"\n");
        }
        writer.close();
    }

}
