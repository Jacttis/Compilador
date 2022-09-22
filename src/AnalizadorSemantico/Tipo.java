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
    public boolean es_de_tipo(Tipo tipo) {
       /* boolean toReturn = false;
        if(!tipo.getToken().getLexeme().equals(tokenTipo.getLexeme())) {
            LinkedList<String> lista_ancestros = new LinkedList<String>();
            TablaDeSimbolos.tablaSimbolos.getClaseByName(tokenTipo.getLexeme()).get_lista_ancestros(lista_ancestros); //Puede andar mal por q no se si se puede ejecutar antes de haber verificado que la clase estaba declarada
            if(lista_ancestros.contains(tipo.getNombre()))
                toReturn = true;
        }
        else toReturn = true;

        return toReturn;*/
        return false;
    }
}
