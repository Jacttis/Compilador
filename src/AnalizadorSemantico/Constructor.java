package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

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
            TablaDeSimbolos.listaExcepciones.add( new SemanticException(argumento.getToken(), "Error Semantico en linea "
                    + argumento.getToken().getNumberline() + ": Ya hay un argumento declarado con el nombre " + argumento.getToken().getLexeme()));
        }
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void checkDeclaracion(Clase claseActual) throws SemanticException {
        LinkedList<String> parametrosVisitados = new LinkedList<>();
        for (Parametro param:listaArgumentos) {
            if(!parametrosVisitados.contains(param.getNombre())){
                param.checkDeclaracion(claseActual);
                param.setOffset(offsetDisponibleParametro);
                offsetDisponibleParametro++;
            }
            else{
                TablaDeSimbolos.listaExcepciones.add( new SemanticException(param.getToken(), "Error Semantico en linea "
                        + param.getToken().getNumberline() + ": parametro con nombre repetido " + param.getToken().getLexeme()));
            }

            parametrosVisitados.add(param.getNombre());
        }
    }

    public String generarEtiqueta(){
        String etiqueta = "LCons"+token.getLexeme();
        for (Parametro parametro:listaArgumentos) {
            etiqueta += "_"+parametro.getTipo().getToken().getLexeme();
        }
        return etiqueta;
    }

    public void generarCodigo() {
    }
}
