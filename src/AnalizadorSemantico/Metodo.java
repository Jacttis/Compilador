package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Metodo extends MetodoConstructor {

    protected Token tokenMetodo;

    protected boolean estatico;
    protected Tipo tipo;

    public Metodo(Token token, boolean estatico, Tipo tipo) {
        super();
        this.tokenMetodo = token;
        this.estatico = estatico;
        this.tipo = tipo;
        //tabla_variables = new HashMap<String,EntradaVariable>();
    }


    @Override
    public void setArgumento(String nombre, Parametro argumento) throws SemanticException {

    }

    @Override
    public void esta_bien_declarado() throws SemanticException {

    }

    public Token getTokenMetodo() {
        return tokenMetodo;
    }

    public void setTokenMetodo(Token tokenMetodo) {
        this.tokenMetodo = tokenMetodo;
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
}
