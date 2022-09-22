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
    public Tipo getTipo(){return tipoParametro;};
    public boolean toCompare(Parametro param_a_comparar) {
        return this.tipoParametro.es_de_tipo(param_a_comparar.getTipo());
    }

    /*public void esta_bien_declarado() throws SemanticException {
        if(!tipoParametro.esPrimitivo()){
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipoParametro.getNombre()))
                throw new ExcepcionSemantica(tokenParametro,"Error Semantico en linea "+ tokenParametro.get_nro_linea() +": El tipo del parametro "+ tokenParametro.get_lexema()+" es una clase que no esta declarada.");
        }
    }*/
}
