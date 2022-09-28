package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Parametro {

    private Token tokenParametro;
    private Tipo tipoParametro;

    public Parametro(Token token_parametro, Tipo tipo_parametro){
        this.tokenParametro = token_parametro;
        this.tipoParametro = tipo_parametro;
    }

    public Token getToken(){ return tokenParametro;}

    public String getNombre(){return tokenParametro.getLexeme();}
    public Tipo getTipo(){return tipoParametro;};
    public boolean toCompare(Parametro param_a_comparar) {
        return this.tipoParametro.compareTipo(param_a_comparar.getTipo());
    }

    public void checkDeclaracion(Clase claseActual) throws SemanticException {
        if(tipoParametro.getToken().getDescription().equals("idClase")){
            TipoReferencia tipoR= (TipoReferencia) tipoParametro;
            if (!tipoR.checkTipo(claseActual)){
                throw new SemanticException(tipoR.getToken(), "Error Semantico en linea "
                        + tipoR.getToken().getNumberline() + ": Clase del Argumento" + tipoR.getToken().getLexeme()+" no declarada ");
            }
        }

    }

    /*public void esta_bien_declarado() throws SemanticException {
        if(!tipoParametro.esPrimitivo()){
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipoParametro.getNombre()))
                throw new ExcepcionSemantica(tokenParametro,"Error Semantico en linea "+ tokenParametro.get_nro_linea() +": El tipo del parametro "+ tokenParametro.get_lexema()+" es una clase que no esta declarada.");
        }
    }*/
}
