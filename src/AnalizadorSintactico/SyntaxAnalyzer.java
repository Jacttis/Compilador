package AnalizadorSintactico;

import AST.*;
import AST.Acceso.*;
import AST.Expresion.*;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token actualToken;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException {
        this.lexicalAnalyzer=lexicalAnalyzer;
        actualToken= lexicalAnalyzer.nextToken();
    }

    private void match(String tokenDescription) throws SyntaxException, LexicalException, IOException {
        if (tokenDescription.equals(actualToken.getDescription())){
            actualToken = lexicalAnalyzer.nextToken();
        }
        else {
            throw new SyntaxException(actualToken,tokenDescription);
        }


    }
    public void inicial() throws LexicalException, SyntaxException, IOException, SemanticException {
        listaClases();
        match("EOF");


    }

    private void listaClases() throws LexicalException, SyntaxException, IOException, SemanticException {
        if(actualToken.getDescription().equals("pr_class") ||actualToken.getDescription().equals("pr_interface") ){
            clase();
            listaClases();
        }
        else{
            //vacio
        }
            
    }

    private void clase() throws LexicalException, SyntaxException, IOException, SemanticException {
        if(actualToken.getDescription().equals("pr_class")){
            claseConcreta();
            
        } else if (actualToken.getDescription().equals("pr_interface") ) {
            interfaz();
        }
        else {
            throw new SyntaxException(actualToken,"un class o interface");
        }
    }

    private void checkCorrectGenericity(LinkedList<Token> parametrosGenericos) throws SemanticException {
        LinkedList<Token> aBorrar=new LinkedList<>();
        if(!parametrosGenericos.isEmpty()) {
            parametrosGenericos.removeFirst();
            parametrosGenericos.removeFirst();
            parametrosGenericos.removeLast();
            for (Token token : parametrosGenericos) {
                if (Arrays.asList("idClase").contains(token.getDescription())) {

                } else if (Arrays.asList("coma").contains(token.getDescription())) {
                    aBorrar.add(token);
                } else {
                    throw new SemanticException(token, "Error Semantico en linea "
                            + token.getNumberline() + ": No se permiten genericidad anidada " + token.getLexeme());
                }
            }
            for (Token t:aBorrar) {
                parametrosGenericos.remove(t);
            }
        }
    }
    private void setearClaseActualGenericidad(LinkedList<Token> parametrosGenericos) throws SemanticException {
        checkCorrectGenericity(parametrosGenericos);
        for (Token token:parametrosGenericos) {
            TablaDeSimbolos.tablaSimbolos.getClaseActual().agregarParametro(token);
        }
    }

    private void interfaz() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_interface");
        Interfaz interfazActual=new Interfaz(actualToken);
        LinkedList<Token> tokensGenericos=new LinkedList<>();
        LinkedList<Token> tokensGenericosTerminados=claseGenerica(tokensGenericos);
        TablaDeSimbolos.tablaSimbolos.setClaseActual(interfazActual);
        if(tokensGenericosTerminados.size()>1){
            setearClaseActualGenericidad(tokensGenericosTerminados );
        }
        LinkedList<LinkedList<Token>> interfazExtendidas = extiendeA();
        if(interfazExtendidas!=null){
            setearInterfaces(interfazExtendidas);
        }
        match("abreCorchete");
        listaEncabezado();
        match("cierraCorchete");
        TablaDeSimbolos.tablaSimbolos.agregarInterfaz(interfazActual.getToken().getLexeme(),interfazActual);
    }

    private void setearInterfaces(LinkedList<LinkedList<Token>> interfazExtendidas) throws SemanticException {
        for (LinkedList<Token> interfaz:interfazExtendidas) {
            Token tokenLexema = interfaz.getFirst();
            if(interfaz.size()>1) {
                checkCorrectGenericity(interfaz);
            }
            else{
                interfaz.removeFirst();
            }
            TablaDeSimbolos.tablaSimbolos.getClaseActual().agregarInterfaz(tokenLexema,interfaz);
        }

    }

    private void listaEncabezado() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_static","pr_boolean","pr_int","pr_char","idClase","pr_void").contains(actualToken.getDescription())) {
           encabezadoMetodo();
           match("puntoComa");
           listaEncabezado();
        }
        else {
            //vacio
        }
    }

    private void encabezadoMetodo() throws LexicalException, SyntaxException, IOException, SemanticException {
        Metodo metodo;
        if (Arrays.asList("pr_static").contains(actualToken.getDescription())){
            estaticoOpt();
            Tipo tipo=tipoMetodo();
            metodo=new Metodo(actualToken,true,tipo);
            TablaDeSimbolos.tablaSimbolos.setMetodoActual(metodo);
            match("idMetVar");
            argsFormales();
            TablaDeSimbolos.tablaSimbolos.getClaseActual().agregarMetodo(metodo.getTokenMetodo().getLexeme(),metodo);
        }else{
            Tipo tipo =tipoMetodo();
            metodo=new Metodo(actualToken,false,tipo);
            TablaDeSimbolos.tablaSimbolos.setMetodoActual(metodo);
            match("idMetVar");
            argsFormales();
            TablaDeSimbolos.tablaSimbolos.getClaseActual().agregarMetodo(metodo.getTokenMetodo().getLexeme(),metodo);
        }
    }

    private void argsFormales() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("abreParentesis");
        listaArgsFormalesOpt();
        match("cierraParentesis");
    }

    private void listaArgsFormalesOpt() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_boolean","pr_char","pr_int","idClase").contains(actualToken.getDescription())) {
            listaArgsFormales();
        }
        else{
            //vacio
        }
    }

    private void listaArgsFormales() throws LexicalException, SyntaxException, IOException, SemanticException {
        argFormal();
        listaArgsFormalesPrima();
    }

    private void argFormal() throws LexicalException, SyntaxException, IOException, SemanticException {
        Tipo tipo=tipo();
        Parametro parm=new Parametro(actualToken,tipo);
        TablaDeSimbolos.tablaSimbolos.getMetodoActual().addArgumento(parm);
        match("idMetVar");
    }

    private void listaArgsFormalesPrima() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaArgsFormales();
        }
        else{
            //vacio
        }
    }

    private Tipo tipoMetodo() throws LexicalException, SyntaxException, IOException, SemanticException {
       Tipo tipo;
        if (Arrays.asList("pr_boolean","pr_int","pr_char","idClase").contains(actualToken.getDescription())) {
            tipo=tipo();

        }
        else{
            tipo=new Tipo(actualToken);
            match("pr_void");
        }
        return tipo;
    }

    private void estaticoOpt() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_static").contains(actualToken.getDescription())) {
            match("pr_static");
        }
        else{

        }
    }

    private LinkedList<LinkedList<Token>> extiendeA() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_extends").contains(actualToken.getDescription())) {
            match("pr_extends");
            LinkedList<LinkedList<Token>> tokensGenericos = new LinkedList<>();
            return listaClaseGenerica(tokensGenericos);
        }
        else{
            return null;
        }
    }

    private void claseConcreta() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_class");
        Clase actualClase=new Clase(actualToken);
        TablaDeSimbolos.tablaSimbolos.setClaseActual(actualClase);
        LinkedList<Token> tokensGenericos=new LinkedList<>();
        LinkedList<Token> tokensGenericosTerminado=claseGenerica(tokensGenericos);
        if(tokensGenericosTerminado.size()>1){
        setearClaseActualGenericidad(tokensGenericosTerminado);
        }
        LinkedList<Token> padre=heredaDe();
        actualClase.setClaseHerencia(padre.getFirst());
        if (padre.size()>1){
            checkCorrectGenericity(padre);
            actualClase.setParametrosPadre(padre);
        }
        LinkedList<LinkedList<Token>> interfazImplementadas= implementaA();
        if(interfazImplementadas!=null){
            setearInterfaces(interfazImplementadas);
        }
        match("abreCorchete");
        listaMiembro();
        match("cierraCorchete");
        TablaDeSimbolos.tablaSimbolos.agregarClase(actualClase.getToken().getLexeme(),actualClase);
    }



    private void listaMiembro() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_public","pr_private","pr_static","pr_void","pr_boolean","pr_char","pr_int","idClase").contains(actualToken.getDescription())) {
            miembro();
            listaMiembro();
        }
        else {
            //vacio
        }
    }

    private void miembro() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_public","pr_private").contains(actualToken.getDescription())) {
            atributo();
        }
        else if(Arrays.asList("pr_static","pr_void").contains(actualToken.getDescription())){
            metodo();
        } else if (Arrays.asList("pr_boolean","pr_char","pr_int","idClase").contains(actualToken.getDescription())) {
            constructor_atributo_metodo();
        }
        else {
            throw new SyntaxException(actualToken,"visibilidad,static o un Tipo");
        }

    }

    private void  constructor_atributo_metodo() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("idClase").contains(actualToken.getDescription())) {
            TipoReferencia tipo= (TipoReferencia) tipo();
            if (Arrays.asList("idMetVar").contains(actualToken.getDescription())) {
                metodo_atributoSinVisibilidad(tipo);
                //metodooatributo
            }
            else {
                if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
                    //constructor
                    if (tipo.getParametrosGenericos().size()==0) {
                        Constructor constructor = new Constructor(tipo.getToken());
                        TablaDeSimbolos.tablaSimbolos.setMetodoActual(constructor);
                        argsFormales();
                        Clase claseActual= (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual();
                        claseActual.agregarConstructor(constructor);
                       NodoBloque bloque = bloque(null);
                        TablaDeSimbolos.tablaSimbolos.getMetodoActual().setBloquePrincipal(bloque);
                    }
                    else {
                        throw new SemanticException(tipo.getToken(), "Error Semantico en linea "
                                + tipo.getToken().getNumberline() + ": No se permite genericidad en los constructores " + tipo.getToken().getLexeme());

                    }
                }
                else{
                    throw new SyntaxException(actualToken,"variable o {");
                }
            }
        }
        else{
           Tipo tipo = tipo();
            metodo_atributoSinVisibilidad(tipo);
        }
    }

    private void metodo_atributoSinVisibilidad(Tipo tipo) throws LexicalException, SyntaxException, IOException, SemanticException {
        Token metodoOAtributo=actualToken;
        match("idMetVar");
        if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            Metodo metodo =new Metodo(metodoOAtributo,false,tipo);
            TablaDeSimbolos.tablaSimbolos.setMetodoActual(metodo);
            argsFormales();
            TablaDeSimbolos.tablaSimbolos.getClaseActual().agregarMetodo(metodo.getTokenMetodo().getLexeme(),metodo);
            NodoBloque bloque=bloque(null);
            TablaDeSimbolos.tablaSimbolos.getMetodoActual().setBloquePrincipal(bloque);
        }
        else{
            String visibilidad="public";
            Atributo atributo=new Atributo(metodoOAtributo,visibilidad,tipo);
            Clase claseActual= (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual();
            claseActual.agregarAtributo(atributo.getTokenAtributo().getLexeme(),atributo);
            listaDecAtrsSinVisibilidad(visibilidad,tipo);
            match("puntoComa");
        }


    }

    private void listaDecAtrsSinVisibilidad(String visibilidad,Tipo tipo) throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaDecAtrs(visibilidad,tipo);
        }
    }

    private void metodo() throws LexicalException, SyntaxException, IOException, SemanticException {
        encabezadoMetodo();
        NodoBloque bloque=bloque(null);
        TablaDeSimbolos.tablaSimbolos.getMetodoActual().setBloquePrincipal(bloque);
    }

    private NodoBloque bloque(NodoBloque bloqueMetodoActual) throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("abreCorchete").contains(actualToken.getDescription())) {
            match("abreCorchete");
            NodoBloque bloque=new NodoBloque(TablaDeSimbolos.tablaSimbolos.getMetodoActual());
            if (bloqueMetodoActual!=null) {
                bloque.setBloqueContainer(bloqueMetodoActual);
            }
            TablaDeSimbolos.tablaSimbolos.setBloqueActual(bloque);
            listaSentencias();
            match("cierraCorchete");
            TablaDeSimbolos.tablaSimbolos.setBloqueActual(bloqueMetodoActual);
            return bloque;

        }
        else{
            return null;
            //vacio
        }
    }

    private void listaSentencias() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("puntoComa","pr_this","idMetVar","pr_new","idClase", "abreParentesis","pr_var","pr_return","pr_if","pr_while","abreCorchete","pr_int","pr_boolean","pr_char").contains(actualToken.getDescription())) {
           NodoSentencia sentencia= sentencia();
           TablaDeSimbolos.tablaSimbolos.getBloqueActual().addSentencia(sentencia);
            listaSentencias();
        }
        else{
            //vacio
        }
    }

    private NodoSentencia sentencia() throws LexicalException, SyntaxException, IOException, SemanticException {
        NodoSentencia sentencia;
        if (Arrays.asList("puntoComa").contains(actualToken.getDescription())) {
            sentencia=null;
            match("puntoComa");
        } else if (Arrays.asList("idClase").contains(actualToken.getDescription())) {
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            Token token=actualToken;
            claseGenerica(tokensGenericos);
            if (Arrays.asList("punto").contains(actualToken.getDescription())){
                //Acceso estatico
                NodoAcceso acceso=acceso(token);
                sentencia=asignacion(acceso);
                match("puntoComa");
            }
            else{
                NodoVarLocalesMismoTipo varLocalesMismoTipo=new NodoVarLocalesMismoTipo();
                varLocalesMismoTipo.setVarLocals(varLocalTipoClase(token));
                sentencia =varLocalesMismoTipo;
                match("puntoComa");
            }
            
        } else if (Arrays.asList("pr_this","idMetVar","pr_new","idClase", "abreParentesis").contains(actualToken.getDescription())) {
            NodoAcceso acceso=acceso(null);
            sentencia=asignacion(acceso);
            match("puntoComa");
        } else if (Arrays.asList("pr_var","pr_int","pr_boolean","pr_char").contains(actualToken.getDescription())) {
            NodoVarLocalesMismoTipo varLocalesMismoTipo=new NodoVarLocalesMismoTipo();
            varLocalesMismoTipo.setVarLocals(varLocal());
            sentencia =varLocalesMismoTipo;
            match("puntoComa");
        } else if (Arrays.asList("pr_return").contains(actualToken.getDescription())) {
            sentencia=retorno();
        } else if (Arrays.asList("pr_if").contains(actualToken.getDescription())) {
            sentencia=If();
        } else if (Arrays.asList("pr_while").contains(actualToken.getDescription())) {
            sentencia=While();
        } else if (Arrays.asList("abreCorchete").contains(actualToken.getDescription())) {
            sentencia=bloque(TablaDeSimbolos.tablaSimbolos.getBloqueActual());
        }
        else {
            throw new SyntaxException(actualToken,"sentencia");
        }
        return sentencia;
    }

    private LinkedList<NodoVarLocal> varLocalTipoClase(Token token) throws LexicalException, SyntaxException, IOException, SemanticException {
        LinkedList<NodoVarLocal> varLocalMismoTipo=new LinkedList<>();
        listaDecVars(varLocalMismoTipo);
        if (Arrays.asList("=").contains(actualToken.getDescription())) {
            match("=");
           NodoExpresion expresion= expresion();
           for (NodoVarLocal varLocal:varLocalMismoTipo) {
               varLocal.setExpresion(expresion);
               varLocal.setTipo(new TipoReferencia(token));
               TablaDeSimbolos.tablaSimbolos.getBloqueActual().addVarLocal(varLocal);
           }
        } else if (Arrays.asList("puntoComa").contains(actualToken.getDescription())) {
            for (NodoVarLocal varLocal:varLocalMismoTipo) {
                varLocal.setTipo(new TipoReferencia(token));
                TablaDeSimbolos.tablaSimbolos.getBloqueActual().addVarLocal(varLocal);
            }
        }
        return varLocalMismoTipo;
    }

    private NodoWhile While() throws LexicalException, SyntaxException, IOException, SemanticException {
        Token token=actualToken;
        match("pr_while");
        match("abreParentesis");
        NodoExpresion expresion=expresion();
        match("cierraParentesis");
        NodoSentencia sentencia=sentencia();
        return new NodoWhile(token,expresion,sentencia);
    }

    private NodoIf If() throws LexicalException, SyntaxException, IOException, SemanticException {
        NodoIf nodoIf;
        Token token=actualToken;
        match("pr_if");
        match("abreParentesis");
        NodoExpresion expresion=expresion();
        match("cierraParentesis");
        NodoSentencia sentencia=sentencia();
        NodoElse nodoElse=Else();
        nodoIf=new NodoIf(token,expresion,sentencia);
        nodoIf.setNodoElse(nodoElse);
        return nodoIf;
    }

    private NodoElse Else() throws LexicalException, SyntaxException, IOException, SemanticException {
        NodoElse nodoElse=null;
        if (Arrays.asList("pr_else").contains(actualToken.getDescription())) {
            match("pr_else");
            NodoSentencia sentencia=sentencia();
            nodoElse=new NodoElse(sentencia);
        }
        else{
            //vacio
        }
        return nodoElse;
    }

    private NodoReturn retorno() throws LexicalException, SyntaxException, IOException {
        NodoReturn nodoReturn;
        Token tokenRet=actualToken;
        match("pr_return");
        NodoExpresion expresion=expresionOpt();
        nodoReturn=new NodoReturn(tokenRet,expresion, (Metodo) TablaDeSimbolos.tablaSimbolos.getMetodoActual());
        match("puntoComa");
        return nodoReturn;

    }

    private NodoExpresion expresionOpt() throws LexicalException, SyntaxException, IOException {
        NodoExpresion expresion=null;
        if (Arrays.asList("+","-","!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase","abreParentesis").contains(actualToken.getDescription())) {
            expresion=expresion();
        }
        else{
            //vacio
        }
        return expresion;
    }

    private LinkedList<NodoVarLocal> varLocal() throws LexicalException, SyntaxException, IOException, SemanticException {
        LinkedList<NodoVarLocal> varLocalMismoTipo=new LinkedList<>();
        if (Arrays.asList("pr_var").contains(actualToken.getDescription())) {
            match("pr_var");
            listaDecVars(varLocalMismoTipo);
            match("=");
            NodoExpresion expresion= expresion();
            for (NodoVarLocal varLocal:varLocalMismoTipo) {
                varLocal.setExpresion(expresion);
                TablaDeSimbolos.tablaSimbolos.getBloqueActual().addVarLocal(varLocal);
            }

        }
        else{
            Tipo tipo =tipoPrimitivo();
            listaDecVars(varLocalMismoTipo);
            if (Arrays.asList("=").contains(actualToken.getDescription())) {
                match("=");
                NodoExpresion expresion= expresion();
                for (NodoVarLocal varLocal:varLocalMismoTipo) {
                    varLocal.setExpresion(expresion);
                    varLocal.setTipo(tipo);
                    TablaDeSimbolos.tablaSimbolos.getBloqueActual().addVarLocal(varLocal);
                }
            } else if (Arrays.asList("puntoComa").contains(actualToken.getDescription())) {
                for (NodoVarLocal varLocal:varLocalMismoTipo) {
                    varLocal.setTipo(tipo);
                    TablaDeSimbolos.tablaSimbolos.getBloqueActual().addVarLocal(varLocal);
                }
            }
        }
        return varLocalMismoTipo;
    }


    private NodoAccesoAsignacion asignacion(NodoAcceso acceso) throws LexicalException, SyntaxException, IOException {
        NodoAccesoAsignacion accesoAsignacion=null;
        if (Arrays.asList("=","+=","-=").contains(actualToken.getDescription())) {
            Token tokenAsignacion=tipoDeAsignacion();
            NodoExpresion expresion=expresion();
            accesoAsignacion=new NodoAccesoAsignacion(acceso,tokenAsignacion,expresion);
        }
        else {
            accesoAsignacion=new NodoAccesoAsignacion(acceso,null,null);
        }
        return accesoAsignacion;
    }

    private NodoExpresion  expresion() throws LexicalException, SyntaxException, IOException {
        NodoExpresionUnaria expresionUnaria=expresionUnaria();
        return expresionPrima(expresionUnaria);
    }

    private NodoExpresion expresionPrima(NodoExpresion expresionIzq) throws LexicalException, SyntaxException, IOException {
        NodoExpresion expresion;
        if (Arrays.asList("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*",  "/", "%").contains(actualToken.getDescription())) {
            NodoExpresionBinaria expresionBinaria;
            Token tokenOperadorBinario=operadorBinario();
            NodoExpresionUnaria expresionDer=expresionUnaria();
            expresionBinaria=new NodoExpresionBinaria(tokenOperadorBinario);
            expresionBinaria.setExpresionIzq(expresionIzq);
            expresionBinaria.setExpresionDer(expresionDer);
            expresion=expresionPrima(expresionBinaria);
            return expresion;
        }
        else{
            return expresionIzq;
            //vacio
        }
    }

    private Token operadorBinario() throws LexicalException, SyntaxException, IOException {
        Token token;
        if (Arrays.asList("||").contains(actualToken.getDescription())) {
            token=actualToken;
            match("||");
        } else if (Arrays.asList("&&").contains(actualToken.getDescription())) {
            token=actualToken;
            match("&&");
        } else if (Arrays.asList("==").contains(actualToken.getDescription())) {
            token=actualToken;
            match("==");
        } else if (Arrays.asList("!=").contains(actualToken.getDescription())) {
            token=actualToken;
            match("!=");
        } else if (Arrays.asList("<").contains(actualToken.getDescription())) {
            token=actualToken;
            match("<");
        } else if (Arrays.asList(">").contains(actualToken.getDescription())) {
            token=actualToken;
            match(">");
        } else if (Arrays.asList("<=").contains(actualToken.getDescription())) {
            token=actualToken;
            match("<=");
        }  else if (Arrays.asList(">=").contains(actualToken.getDescription())) {
            token=actualToken;
            match(">=");
        } else if (Arrays.asList("+").contains(actualToken.getDescription())) {
            token=actualToken;
            match("+");
        } else if (Arrays.asList("-").contains(actualToken.getDescription())) {
            token=actualToken;
            match("-");
        } else if (Arrays.asList("*").contains(actualToken.getDescription())) {
            token=actualToken;
            match("*");
        } else if (Arrays.asList("/").contains(actualToken.getDescription())) {
            token=actualToken;
            match("/");
        } else if (Arrays.asList("%").contains(actualToken.getDescription())) {
            token=actualToken;
            match("%");
        }
        else {
            throw new SyntaxException(actualToken,"operador binario");
        }
        return token;
    }

    private NodoExpresionUnaria expresionUnaria() throws LexicalException, SyntaxException, IOException {
        NodoExpresionUnaria expresionUnaria;
        if (Arrays.asList("+","-","!").contains(actualToken.getDescription())) {
            Token operadorUnarioToken=operadorUnario();
            NodoOperando nodoOperando=operando();
            expresionUnaria=new NodoExpresionUnaria(operadorUnarioToken,nodoOperando);
         }else if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
             NodoOperando nodoOperando=operando();
            expresionUnaria=new NodoExpresionUnaria(null,nodoOperando);
         }
         else {
             throw new SyntaxException(actualToken,"una expresion unaria");
         }
         return expresionUnaria;

    }

    private NodoOperando operando() throws LexicalException, SyntaxException, IOException {
       NodoOperando nodoOperando;
        if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(actualToken.getDescription())) {
            nodoOperando=literal();
        }else if (Arrays.asList("pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
            NodoAcceso nodoAcceso=acceso(null);
            nodoOperando=new NodoOperandoAcceso(nodoAcceso);
        }
        else {
            throw new SyntaxException(actualToken,"un operando");
        }
        return nodoOperando;
    }

    private NodoOperandoLiteral literal() throws LexicalException, SyntaxException, IOException {
        NodoOperandoLiteral nodoOperandoLiteral;
        if (Arrays.asList("pr_null").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoReferencia(actualToken));
            match("pr_null");
        } else if (Arrays.asList("pr_true").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoPrimitivo(new Token("pr_boolean","boolean",actualToken.getNumberline())));
            match("pr_true");
        } else if (Arrays.asList("pr_false").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoPrimitivo(new Token("pr_boolean","boolean",actualToken.getNumberline())));
            match("pr_false");
        } else if (Arrays.asList("intLiteral").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoPrimitivo(new Token("pr_int","int",actualToken.getNumberline())));
            match("intLiteral");
        } else if (Arrays.asList("charLiteral").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoPrimitivo(new Token("pr_char","char",actualToken.getNumberline())));
            match("charLiteral");
        } else if (Arrays.asList("stringLiteral").contains(actualToken.getDescription())) {
            nodoOperandoLiteral= new NodoOperandoLiteral(actualToken,new TipoReferencia(new Token("idClase","String",actualToken.getNumberline())));
            match("stringLiteral");
        }
        else {
            throw new SyntaxException(actualToken,"un literal");
        }
        return nodoOperandoLiteral;
    }

    private Token operadorUnario() throws LexicalException, SyntaxException, IOException {
        Token operadorUnario;
        if (Arrays.asList("+").contains(actualToken.getDescription())) {
            operadorUnario=actualToken;
            match("+");
        } else if (Arrays.asList("-").contains(actualToken.getDescription())) {
            operadorUnario=actualToken;
            match("-");
        } else if (Arrays.asList("!").contains(actualToken.getDescription())) {
            operadorUnario=actualToken;
            match("!");
        }
        else {
            throw new SyntaxException(actualToken,"un operador unario");
        }
        return operadorUnario;
    }

    private Token tipoDeAsignacion() throws LexicalException, SyntaxException, IOException {
        Token tokenAsignacion;
        if (Arrays.asList("=").contains(actualToken.getDescription())){
            tokenAsignacion=actualToken;
            match("=");
        } else if (Arrays.asList("+=").contains(actualToken.getDescription())) {
            tokenAsignacion=actualToken;
            match("+=");
        } else if (Arrays.asList("-=").contains(actualToken.getDescription())) {
            tokenAsignacion=actualToken;
            match("-=");
        }
        else {
            throw new SyntaxException(actualToken,"una asignacion");
        }
        return tokenAsignacion;
    }

    private NodoAcceso acceso(Token token) throws LexicalException, SyntaxException, IOException {
        NodoAcceso nodoAcceso;
        nodoAcceso=primario(token);
        NodoEncadenado encadenado= encadenadoOpt();
        nodoAcceso.setNodoEncadenado(encadenado);
        return nodoAcceso;
    }

    private NodoEncadenado encadenadoOpt() throws LexicalException, SyntaxException, IOException {
        Token tokenMetVar;
        NodoEncadenado encadenado=null;
        if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            match("punto");
            tokenMetVar=actualToken;
            match("idMetVar");
            encadenado=encadenadoOptPrima(tokenMetVar);
        }
        else{

        }
        return encadenado;
    }

    private NodoEncadenado encadenadoOptPrima(Token token) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            NodoVariableEncadenada encadenado;
            encadenado=new NodoVariableEncadenada(token);
            NodoEncadenado encadenadoPrima=encadenadoOpt();
            encadenado.setNodoEncadenado(encadenadoPrima);
            return encadenado;
        } else if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            NodoMetodoEncadenado encadenado;
            LinkedList<NodoExpresion> expresiones=argsActuales();
            encadenado=new NodoMetodoEncadenado(token);
            encadenado.setParametros(expresiones);
            NodoEncadenado encadenadoPrima=encadenadoOpt();
            encadenado.setNodoEncadenado(encadenadoPrima);
            return encadenado;
        } else{
            NodoVariableEncadenada encadenado;
            encadenado=new NodoVariableEncadenada(token);
            return encadenado;
        }
    }

    private NodoAcceso primario(Token token) throws LexicalException, SyntaxException, IOException {
        NodoAcceso nodoAcceso;
        if (Arrays.asList("pr_this").contains(actualToken.getDescription())) {
            nodoAcceso=accesoThis();
        } else if (Arrays.asList("idMetVar").contains(actualToken.getDescription())) {
            Token tokenM=actualToken;
            match("idMetVar");
            nodoAcceso=accesoMetodo(tokenM);
        } else if (Arrays.asList("pr_new").contains(actualToken.getDescription())) {
            nodoAcceso=accesoConstructor();
        } else if (Arrays.asList("idClase","punto").contains(actualToken.getDescription())) {
            nodoAcceso=accesoMetodoEstatico(token);
        } else if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            nodoAcceso=expresionParentizada();
        }
        else {
            throw new SyntaxException(actualToken,"un this,id de Metodo o variable, un new,un . o un (");
        }

        return nodoAcceso;
    }

    private NodoAccesoExpresionParentizada expresionParentizada() throws LexicalException, SyntaxException, IOException {
        match("abreParentesis");
        NodoExpresion expresion=expresion();
        NodoAccesoExpresionParentizada expresionParentizada=new NodoAccesoExpresionParentizada(expresion);
        match("cierraParentesis");

        return expresionParentizada;
    }

    private LinkedList<NodoExpresion> listaExpsOpt() throws LexicalException, SyntaxException, IOException {
       LinkedList<NodoExpresion> expresiones=new LinkedList<>();
        if (Arrays.asList( "+","-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
            listaExps(expresiones);
        }
        else{

        }

    return expresiones;
    }

    private void listaExps(LinkedList<NodoExpresion> expresiones) throws LexicalException, SyntaxException, IOException {
        NodoExpresion expresion=expresion();
        expresiones.add(expresion);
        listaExpsPrima(expresiones);
    }

    private void listaExpsPrima(LinkedList<NodoExpresion> expresiones) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaExps(expresiones);
        }
        else{

        }
    }

    private NodoAccesoMetodoEstatico accesoMetodoEstatico(Token tokenIdClase) throws LexicalException, SyntaxException, IOException {
        NodoAccesoMetodoEstatico metodoEstatico;
        if (Arrays.asList("idClase").contains(actualToken.getDescription())) {
            tokenIdClase=actualToken;
            match("idClase");
            match("punto");
            Token metodo=actualToken;
            match("idMetVar");
            metodoEstatico=new NodoAccesoMetodoEstatico(metodo,tokenIdClase);
            LinkedList<NodoExpresion> parametros=argsActuales();
            metodoEstatico.setParametros(parametros);
        } else if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            match("punto");
            Token metodo=actualToken;
            match("idMetVar");
            metodoEstatico=new NodoAccesoMetodoEstatico(metodo,tokenIdClase);
            LinkedList<NodoExpresion> parametros=argsActuales();
            metodoEstatico.setParametros(parametros);
        }
        else {
            throw new SyntaxException(actualToken,"un punto o un idClase");
        }
        return metodoEstatico;
    }



    private LinkedList<NodoExpresion> argsActuales() throws LexicalException, SyntaxException, IOException {
        LinkedList<NodoExpresion> expresiones;
        match("abreParentesis");
        expresiones= listaExpsOpt();
        match("cierraParentesis");
        return expresiones;
    }

    private NodoAcceso accesoMetodo(Token token) throws LexicalException, SyntaxException, IOException {
        NodoAcceso accesoMetodo;
        if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            accesoMetodo=new NodoAccesoMetodo(token, (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual(),TablaDeSimbolos.tablaSimbolos.getMetodoActual());
            LinkedList<NodoExpresion> parametros=argsActuales();
            ((NodoAccesoMetodo) accesoMetodo).setParametros(parametros);
        }
        else{
            accesoMetodo=new NodoAccesoVarParam(token, (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual(),TablaDeSimbolos.tablaSimbolos.getMetodoActual(),TablaDeSimbolos.tablaSimbolos.getBloqueActual());
        }
        return accesoMetodo;
    }

    private NodoAccesoConstructor accesoConstructor() throws LexicalException, SyntaxException, IOException {
        NodoAccesoConstructor accesoConstructor;
        match("pr_new");
        Token token=claseGenericaConstructor();
        accesoConstructor=new NodoAccesoConstructor(token);
        LinkedList<NodoExpresion> parametros=argsActuales();
        accesoConstructor.setParametros(parametros);
        return accesoConstructor;
    }
    
    private NodoAccesoThis accesoThis() throws LexicalException, SyntaxException, IOException {
        NodoAccesoThis accesoThis=new NodoAccesoThis(actualToken, (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual(),TablaDeSimbolos.tablaSimbolos.getMetodoActual());
        match("pr_this");
        return accesoThis;
    }

    private void atributo() throws LexicalException, SyntaxException, IOException, SemanticException {
       String visibilidad = visibilidad();
        Tipo tipo=tipo();
        listaDecAtrs(visibilidad,tipo);
        match("puntoComa");
    }

    private void listaDecAtrs(String visibilidad,Tipo tipo) throws LexicalException, SyntaxException, IOException, SemanticException {
        Atributo atributo=new Atributo(actualToken,visibilidad,tipo);
        match("idMetVar");
        Clase claseActual= (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual();
        claseActual.agregarAtributo(atributo.getTokenAtributo().getLexeme(),atributo);
        listaDecAtrsPrima(visibilidad,tipo);
    }

    private void listaDecAtrsPrima(String visibilidad,Tipo tipo) throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaDecAtrs(visibilidad,tipo);
        }
        else{
            //vacio
        }
    }

    private void listaDecVars(LinkedList<NodoVarLocal> varLocals) throws LexicalException, SyntaxException, IOException, SemanticException {
        varLocals.add(new NodoVarLocal(actualToken,null,TablaDeSimbolos.tablaSimbolos.getBloqueActual(), TablaDeSimbolos.tablaSimbolos.getMetodoActual(), (Clase) TablaDeSimbolos.tablaSimbolos.getClaseActual()));
        match("idMetVar");
        listaDecVarsPrima(varLocals);
    }

    private void listaDecVarsPrima(LinkedList<NodoVarLocal> varLocals) throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaDecVars(varLocals);
        }
        else{
            //vacio
        }
    }

    private Tipo tipo() throws LexicalException, SyntaxException, IOException, SemanticException {
        Tipo tipo;
        if (Arrays.asList("pr_boolean","pr_char","pr_int").contains(actualToken.getDescription())) {
            tipo=tipoPrimitivo();
        }
        else if(Arrays.asList("idClase").contains(actualToken.getDescription())){
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            tipo=new TipoReferencia(actualToken);
            seterParametrosATipoReferencia((TipoReferencia) tipo,claseGenerica(tokensGenericos));
        }
        else {
            throw new SyntaxException(actualToken,"un tipo");
        }
        return tipo;
    }

    private void seterParametrosATipoReferencia(TipoReferencia tipoR,LinkedList<Token> parametrosGenericos) throws SemanticException {
        if(parametrosGenericos.size()>1) {
            checkCorrectGenericity(parametrosGenericos);
            for (Token token : parametrosGenericos) {
                tipoR.agregarParametro(token);
            }
        }
    }

    private Tipo tipoPrimitivo() throws LexicalException, SyntaxException, IOException {
        Tipo tipo;
        if (Arrays.asList("pr_boolean").contains(actualToken.getDescription())) {
            tipo=new TipoPrimitivo(actualToken);
            match("pr_boolean");
        }
        else if(Arrays.asList("pr_char").contains(actualToken.getDescription())){
            tipo=new TipoPrimitivo(actualToken);
            match("pr_char");
        }
        else if (Arrays.asList("pr_int").contains(actualToken.getDescription())){
            tipo=new TipoPrimitivo(actualToken);
            match("pr_int");
        }
        else {
            throw new SyntaxException(actualToken,"un tipo Primitivo");
        }
        return tipo;
    }

    private String visibilidad() throws LexicalException, SyntaxException, IOException {
        String retorno;
        if (Arrays.asList("pr_public").contains(actualToken.getDescription())) {
            retorno=actualToken.getLexeme();
            match("pr_public");
        }
        else if(Arrays.asList("pr_private").contains(actualToken.getDescription())){
            retorno=actualToken.getLexeme();
            match("pr_private");
        }
        else{
            throw new SyntaxException(actualToken,"un public o private");
        }
        return retorno;
    }

    private LinkedList<LinkedList<Token>> implementaA() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_implements").contains(actualToken.getDescription())) {
            match("pr_implements");
            LinkedList<LinkedList<Token>> tokensGenericos=new LinkedList<>();
            return listaClaseGenerica(tokensGenericos);
        }
        else {
            return null;
        }
    }



    private LinkedList<Token> heredaDe() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_extends").contains(actualToken.getDescription())) {
            match("pr_extends");
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            LinkedList<Token>tokensGenericosNueva=claseGenerica(tokensGenericos);
            return tokensGenericosNueva;
        }
        else{
            LinkedList<Token> tokenObject=new LinkedList<>();
            tokenObject.add(new Token("idClase","Object", 0));
            return tokenObject;//PReguntar
        }
    }

    private LinkedList<LinkedList<Token>> listaClaseGenerica(LinkedList<LinkedList<Token>> listaTokensGenericos) throws LexicalException, SyntaxException, IOException {
        LinkedList<Token> tokensGenericos=new LinkedList<>();
        LinkedList<Token> tokenGenericosActualizado= claseGenerica(tokensGenericos);
        listaTokensGenericos.add(tokenGenericosActualizado);
        return listaClaseGenericaPrima(listaTokensGenericos);
    }

    private LinkedList<Token> listaTipoReferencia(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        tokensGenericos.add(actualToken);
        match("idClase");
        return listaTipoReferenciaPrima(tokensGenericos);
    }

    private LinkedList<Token> listaTipoReferenciaPrima(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            tokensGenericos.add(actualToken);
            match("coma");
            return listaTipoReferencia(tokensGenericos);
        }
        else {
            return tokensGenericos;
        }
    }

    private LinkedList<Token> claseGenerica(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        tokensGenericos.add(actualToken);
        match("idClase");
        return pico(tokensGenericos);
    }

    private LinkedList<Token> pico(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("<").contains(actualToken.getDescription())) {
            tokensGenericos.add(actualToken);
            match("<");
            LinkedList<Token> tokensGenericosActualizado=listaTipoReferencia(tokensGenericos);
            tokensGenericosActualizado.add(actualToken);
            match(">");
            return tokensGenericosActualizado;
        }
        else {
            return tokensGenericos;
        }
    }

    private LinkedList<LinkedList<Token>> listaClaseGenericaPrima(LinkedList<LinkedList<Token>> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            return listaClaseGenerica(tokensGenericos);
        }
        else{
            return tokensGenericos;
        }
    }

    private void listaClaseGenericaConstructor() throws LexicalException, SyntaxException, IOException {
        claseGenericaConstructor();
        listaClaseGenericaPrimaConstructor();
    }

    private Token claseGenericaConstructor() throws LexicalException, SyntaxException, IOException {
        Token token=actualToken;
        match("idClase");
        picoConstructor();
        return token;
    }

    private void picoConstructor() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("<").contains(actualToken.getDescription())) {
            match("<");
            if (Arrays.asList("idClase").contains(actualToken.getDescription())){
                listaClaseGenericaConstructor();
                match(">");
            } else if (Arrays.asList(">").contains(actualToken.getDescription())) {
                match(">");
            }
            else {
                throw new SyntaxException(actualToken,"una Clase o un >");
            }
        }
        else {

        }
    }

    private void listaClaseGenericaPrimaConstructor() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaClaseGenericaConstructor();
        }
        else {

        }
    }
    
}
