package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Constructor extends MetodoConstructor{


    public Constructor(Token token){
        super();
        this.token=token;
    }
    @Override
    public void addArgumento(Parametro argumento) throws SemanticException {
        if(!listaArgumentos.contains(argumento)){
            listaArgumentos.add(argumento);
        }
        else{
            throw new SemanticException(argumento.getToken(), "Error Semantico en linea "
                    + argumento.getToken().getNumberline() + ": Ya hay un argumento declarado con el nombre " + argumento.getToken().getLexeme());
        }
    }

    @Override
    public void esta_bien_declarado() throws SemanticException {

    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
