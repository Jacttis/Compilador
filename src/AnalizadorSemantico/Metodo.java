package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Metodo extends MetodoConstructor {


    protected boolean estatico;
    protected Tipo tipo;

    public Metodo(Token token, boolean estatico, Tipo tipo) {
        super();
        this.token = token;
        this.estatico = estatico;
        this.tipo = tipo;
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

    public Token getTokenMetodo() {
        return token;
    }

    public void setTokenMetodo(Token tokenMetodo) {
        this.token = tokenMetodo;
    }

    public boolean isEstatico() {
        return estatico;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void checkDeclaracion(Clase claseActual) throws SemanticException {
        if(tipo.getToken().getDescription().equals("idClase")){
            TipoReferencia tipoR= (TipoReferencia) tipo;
            if (!tipoR.checkTipo(claseActual)){
                throw new SemanticException(tipo.getToken(), "Error Semantico en linea "
                        + tipo.getToken().getNumberline() + ": Clase de retorno no declarada " + tipo.getToken().getLexeme());
            }
        }
        for (Parametro param:listaArgumentos) {
            param.checkDeclaracion(claseActual);
        }
    }
}
