package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class Metodo extends MetodoConstructor {


    protected boolean estatico;
    protected Tipo tipo;
    protected int offset;

    protected Clase claseBase;

    public Metodo(Token token, boolean estatico, Tipo tipo,Clase claseBase) {
        super();
        this.token = token;
        this.estatico = estatico;
        this.tipo = tipo;
        offset = -2;
        this.claseBase=claseBase;

        if (this.estatico) {
            offsetDisponibleParametro = 3;
        } else {
            offsetDisponibleParametro = 4;
        }
    }

    public Metodo(Token token, boolean estatico, Tipo tipo) {
        super();
        this.token = token;
        this.estatico = estatico;
        this.tipo = tipo;
        offset = -2;
        if (estatico) {
            offsetDisponibleParametro = 3;
        } else {
            offsetDisponibleParametro = 4;
        }
    }

    public boolean seAsignoOffset() {
        return offset != -2;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    @Override
    public void addArgumento(Parametro argumento) throws SemanticException {
        if (!listaArgumentos.contains(argumento)) {
            listaArgumentos.add(argumento);
        } else {
            TablaDeSimbolos.listaExcepciones.add(new SemanticException(argumento.getToken(), "Error Semantico en linea "
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
        if (tipo.getToken().getDescription().equals("idClase")) {
            TipoReferencia tipoR = (TipoReferencia) tipo;
            if (!tipoR.checkTipo(claseActual)) {
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(tipo.getToken(), "Error Semantico en linea "
                        + tipo.getToken().getNumberline() + ": Clase de retorno no declarada " + tipo.getToken().getLexeme()));
            }
        }
        LinkedList<String> parametrosVisitados = new LinkedList<>();
        for (Parametro param : listaArgumentos) {
            if (!parametrosVisitados.contains(param.getNombre())) {
                param.checkDeclaracion(claseActual);
                param.setOffset(offsetDisponibleParametro);
                offsetDisponibleParametro++;
            } else {
                TablaDeSimbolos.listaExcepciones.add(new SemanticException(param.getToken(), "Error Semantico en linea "
                        + param.getToken().getNumberline() + ": parametro con nombre repetido " + param.getToken().getLexeme()));
            }

            parametrosVisitados.add(param.getNombre());
        }
    }


    public String generarEtiqueta() {
        StringBuilder etiqueta = new StringBuilder("l" + token.getLexeme());
        if (token.getLexeme().equals("main") && estatico) {
            return "Lmain";
        }
        else{
            for (Parametro parametro:listaArgumentos) {
                etiqueta.append("_").append(parametro.getTipo().getToken().getLexeme());
            }
            etiqueta.append("_").append(claseBase.getToken().getLexeme());
            return etiqueta.toString();
        }
    }
    public boolean esDeClase(String clase){
        return claseBase.getToken().getLexeme().equals(clase);
    }

    public void generarCodigo() {
        TablaDeSimbolos.codigoMaquina.add("LOADFP");
        TablaDeSimbolos.codigoMaquina.add("LOADSP");
        TablaDeSimbolos.codigoMaquina.add("STOREFP");
        switch (token.getLexeme()) {
            case "debugPrint" :{
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("IPRINT");
                TablaDeSimbolos.codigoMaquina.add("PRNLN");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.
                break;
            }
            case "printI" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("IPRINT");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.
                break;
            }
            case "printC" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("CPRINT");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.

                break;
            }
            case "printB" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("BPRINT");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.
                break;
            }
            case "printS" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("SPRINT");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.

                break;
            }
            case "read" : {
                TablaDeSimbolos.codigoMaquina.add("READ");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // no tiene param

                break;
            }
            case "println" : {
                TablaDeSimbolos.codigoMaquina.add("PRNLN");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 0"); // tiene 1 parametro formal.
                break;
            }
            case "printBln" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("BPRINT");
                TablaDeSimbolos.codigoMaquina.add("PRNLN");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.
                break;
            }
            case "printIln" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("IPRINT");
                TablaDeSimbolos.codigoMaquina.add("PRNLN");
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.
                break;
            }
            case "printCln" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("CPRINT");
                TablaDeSimbolos.codigoMaquina.add("PRNLN");

                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.

                break;
            }
            case "printSln" : {
                TablaDeSimbolos.codigoMaquina.add("LOAD 3");
                TablaDeSimbolos.codigoMaquina.add("SPRINT");
                TablaDeSimbolos.codigoMaquina.add("PRNLN");

                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                TablaDeSimbolos.codigoMaquina.add("RET 1"); // tiene 1 parametro formal.

                break;
            }
            default:{
                bloquePrincipal.generarCodigo();
                TablaDeSimbolos.codigoMaquina.add("STOREFP");
                if (estatico){
                    TablaDeSimbolos.codigoMaquina.add("RET "+(listaArgumentos.size()));
                }
                else{
                    TablaDeSimbolos.codigoMaquina.add("RET "+(listaArgumentos.size()+1));
                }
            }

        }
    }
}
