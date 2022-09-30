package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class InterfazGenericas {


    Token token;
    LinkedList<Token> listaParametros;
    public InterfazGenericas(Token token,LinkedList<Token> listaParametros){
        this.token=token;
        this.listaParametros=listaParametros;

    }
}
