package AnalizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    char actualChar;
    int actualLineNumber;
    boolean EOF;
    FileReader fileReader;

    public FileManager(File file) throws FileNotFoundException {
        fileReader=new FileReader(file);
        EOF =false;
        actualLineNumber=1;
    }

    public char getNextChar() throws IOException {
        int actualCharValue= fileReader.read();
        if (actualCharValue != -1){
            actualChar=(char) actualCharValue;
            if(actualChar=='\n'){
                actualLineNumber++;
            }
            return actualChar;
        }
        else{
            EOF=true;
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
}
