package AST;

public class NodoElse{
    protected NodoSentencia sentencia;

    public NodoElse(NodoSentencia sentencia){
        this.sentencia=sentencia;
    }

    public void chequear() {
        sentencia.chequear();
    }
}
