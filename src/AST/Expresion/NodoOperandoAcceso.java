package AST.Expresion;

import AST.Acceso.NodoAcceso;
import AnalizadorSemantico.Tipo;

public class NodoOperandoAcceso extends NodoOperando {

    NodoAcceso acceso;
    public NodoOperandoAcceso(NodoAcceso acceso) {
        super(acceso.getToken());
        this.acceso=acceso;
    }

    @Override
    public Tipo chequear() {
            return acceso.chequear();
    }

    @Override
    public void generarCodigo() {
        acceso.generarCodigo();
    }
}
