package AnalizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    char actualChar;
    int actualLineNumber;
    String actualLine;

    int actualColumnNumber;
    boolean EOF;
    FileReader fileReader;

    public FileManager(File file) throws FileNotFoundException {
        fileReader=new FileReader(file);
        EOF =false;
        actualLineNumber=1;
        actualColumnNumber=0;
        actualLine="";
    }

    public char getNextChar() throws IOException {
        int actualCharValue= fileReader.read();
        if (actualCharValue != -1){
            actualChar=(char) actualCharValue;
            actualLine+=actualChar;
            actualColumnNumber++;
            if(actualChar=='\n'){
                actualLineNumber++;
                actualLine="";
                actualColumnNumber=0;
            }
            return actualChar;
        }
        else{
            EOF=true;
            actualColumnNumber++;
            return ' ';
        }
    }

    public char getActualChar() {
        return actualChar;
    }

    public int getLineNumber() {
        return actualLineNumber;
    }

    public boolean isEOF() {
        return EOF;
    }

    public void setActualChar(char actualChar) {
        this.actualChar = actualChar;
    }

    public int getActualLineNumber() {
        return actualLineNumber;
    }

    public void setActualLineNumber(int actualLineNumber) {
        this.actualLineNumber = actualLineNumber;
    }

    public String getActualLine() {
        return actualLine;
    }

    public void setActualLine(String actualLine) {
        this.actualLine = actualLine;
    }

    public int getActualColumnNumber() {
        return actualColumnNumber;
    }

    public void setActualColumnNumber(int actualColumnNumber) {
        this.actualColumnNumber = actualColumnNumber;
    }

}
