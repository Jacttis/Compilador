package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Atributo {

    private Token tokenAtributo;
    private Tipo tipoAtributo;
    private String visibilidadAtributo;
    private int offset;

    public Atributo (Token token_atributo, String visibilidad_atributo, Tipo tipo_atributo ){
        this.tokenAtributo = token_atributo;
        this.visibilidadAtributo = visibilidad_atributo;
        this.tipoAtributo = tipo_atributo;
    }

    public Token getTokenAtributo() {
        return tokenAtributo;
    }

    public void setTokenAtributo(Token tokenAtributo) {
        this.tokenAtributo = tokenAtributo;
    }

    public Tipo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(Tipo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public String getVisibilidadAtributo() {
        return visibilidadAtributo;
    }

    public void setVisibilidadAtributo(String visibilidadAtributo) {
        this.visibilidadAtributo = visibilidadAtributo;
    }

    public void checkDeclaracion(Clase claseActual) throws SemanticException {
        if (tipoAtributo.tokenTipo.getDescription().equals("idClase")){
            TipoReferencia tipoReferencia = (TipoReferencia) tipoAtributo;
            if(!tipoReferencia.checkTipo(claseActual)){
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(tipoAtributo.getToken(), "Error Semantico en linea "
                        + tipoAtributo.getToken().getNumberline() + ": Tipo de atributo no declarada " + tipoAtributo.getToken().getLexeme()));
            }
        }
    }
}
