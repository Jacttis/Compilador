package AST;

import java.util.LinkedList;

public class NodoVarLocalesMismoTipo extends NodoSentencia{

    protected LinkedList<NodoVarLocal> varLocals;

    public NodoVarLocalesMismoTipo(){}

    public LinkedList<NodoVarLocal> getVarLocals() {
        return varLocals;
    }

    public void setVarLocals(LinkedList<NodoVarLocal> varLocals) {
        this.varLocals = varLocals;
    }

    protected boolean esVar() {
        return true;
    }

    @Override
    public void generarCodigo() {
        for (NodoVarLocal var :varLocals) {
            var.generarCodigo();
        }
    }
}
