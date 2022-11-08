package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class Metodo extends MetodoConstructor {


    protected boolean estatico;
    protected Tipo tipo;
    protected int offset;

    public Metodo(Token token, boolean estatico, Tipo tipo) {
        super();
        this.token = token;
        this.estatico = estatico;
        this.tipo = tipo;
        offset=-2;
    }

    public boolean seAsignoOffset(){
        return offset!=-2;
    }
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }



    @Override
    public void addArgumento(Parametro argumento) throws SemanticException {
        if(!listaArgumentos.contains(argumento)){
            listaArgumentos.add(argumento);
        }
        else{
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(argumento.getToken(), "Error Semantico en linea "
                    + argumento.getToken().getNumberline() + ": Ya hay un argumento declarado con el nombre " + argumento.getToken().getLexeme()));
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

    public void checkDeclaracion(IClaseInterfaz claseActual) throws SemanticException {
        if(tipo.getToken().getDescription().equals("idClase")){
            TipoReferencia tipoR= (TipoReferencia) tipo;
            if (!tipoR.checkTipo(claseActual)){
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(tipo.getToken(), "Error Semantico en linea "
                        + tipo.getToken().getNumberline() + ": Clase de retorno no declarada " + tipo.getToken().getLexeme()));
            }
        }
        LinkedList<String> parametrosVisitados=new LinkedList<>();
        for (Parametro param:listaArgumentos) {
            if(!parametrosVisitados.contains(param.getNombre())){
                param.checkDeclaracion(claseActual);
            }
            else{
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(param.getToken(), "Error Semantico en linea "
                        + param.getToken().getNumberline() + ": parametro con nombre repetido " + param.getToken().getLexeme()));
            }

            parametrosVisitados.add(param.getNombre());
        }
    }
}
