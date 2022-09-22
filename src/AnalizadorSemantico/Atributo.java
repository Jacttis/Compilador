package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Atributo {

    private Token tokenAtributo;
    private Tipo tipoAtributo;
    private String visibilidadAtributo;

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

}
