package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class Tipo {
    public Token getToken() {
        return tokenTipo;
    }

    public void setTokenTipo(Token tokenTipo) {
        this.tokenTipo = tokenTipo;
    }

    protected Token tokenTipo;
    public Tipo(Token actualToken) {
        tokenTipo=actualToken;
    }
    public boolean compareTipo(Tipo tipo) {
        boolean toReturn = false;

        if(!tipo.getToken().getLexeme().equals(tokenTipo.getLexeme())) {
            toReturn=false;
        }
        else toReturn = true;

        return toReturn;

    }


}
