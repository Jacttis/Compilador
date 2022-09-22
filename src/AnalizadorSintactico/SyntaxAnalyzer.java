package AnalizadorSintactico;

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

    private void interfaz() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_interface");
        Interfaz interfazActual=new Interfaz(actualToken);
        TablaDeSimbolos.tablaSimbolos.setClaseActual(interfazActual);
        LinkedList<Token> tokensGenericos=new LinkedList<>();
        LinkedList<Token> tokensGenericosTerminados=claseGenerica(tokensGenericos);
        setearClaseActualGenericidad(); //REVISARRR
        LinkedList<Token>interfazExtendidas = extiendeA(); //ACORDASE DEL HASH
        match("abreCorchete");
        listaEncabezado();
        match("cierraCorchete");
        TablaDeSimbolos.tablaSimbolos.agregarInterfaz(interfazActual.getToken().getLexeme(),interfazActual);
    }

    private void listaEncabezado() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_static","pr_boolean","pr_int","pr_char","idClase","pr_void").contains(actualToken.getDescription())) {
           encabezadoMetodo();
           match("puntoComa");
           listaEncabezado();
        }
        else {
            //vacio
        }
    }

    private void encabezadoMetodo() throws LexicalException, SyntaxException, IOException {
        Metodo metodo;
        if (Arrays.asList("pr_static").contains(actualToken.getDescription())){
            estaticoOpt();
            Tipo tipo=tipoMetodo();
            metodo=new Metodo(actualToken,true,tipo);
            TablaDeSimbolos.tablaSimbolos.setMetodoActual(metodo);
            match("idMetVar");
            argsFormales();
        }else{
            tipoMetodo();
            match("idMetVar");
            argsFormales();
        }
    }

    private void argsFormales() throws LexicalException, SyntaxException, IOException {
        match("abreParentesis");
        listaArgsFormalesOpt();
        match("cierraParentesis");
    }

    private void listaArgsFormalesOpt() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_boolean","pr_char","pr_int","idClase").contains(actualToken.getDescription())) {
            listaArgsFormales();
        }
        else{
            //vacio
        }
    }

    private void listaArgsFormales() throws LexicalException, SyntaxException, IOException {
        argFormal();
        listaArgsFormalesPrima();
    }

    private void argFormal() throws LexicalException, SyntaxException, IOException {
        tipo();
        match("idMetVar");
    }

    private void listaArgsFormalesPrima() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaArgsFormales();
        }
        else{
            //vacio
        }
    }

    private Tipo tipoMetodo() throws LexicalException, SyntaxException, IOException {
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

    private LinkedList<Token> extiendeA() throws LexicalException, SyntaxException, IOException {
            match("pr_extends");
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            return listaClaseGenerica(tokensGenericos);
    }

    private void claseConcreta() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_class");
        Clase actualClase=new Clase(actualToken);
        TablaDeSimbolos.tablaSimbolos.setClaseActual(actualClase);
        LinkedList<Token> tokensGenericos=new LinkedList<>();
        LinkedList<Token> tokensGenericosTerminado=claseGenerica(tokensGenericos);
        setearClaseActualGenericidad(); //preguntar si se puede hacer asi
        Token padre=heredaDe();
        actualClase.setClaseHerencia(padre);
        LinkedList<Token> tokensInterfaces= implementaA();
        //TablaDeSimbolos.tablaSimbolos.set(tokensInteraface)
        match("abreCorchete");
        listaMiembro();
        match("cierraCorchete");
        TablaDeSimbolos.tablaSimbolos.agregarClase(actualClase.getToken().getLexeme(),actualClase);
    }

    private void setearClaseActualGenericidad() {
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
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            LinkedList<Token> tokensGenericosTerminado=claseGenerica(tokensGenericos);
            Tipo tipo=new Tipo(tokensGenericosTerminado.getFirst());
            if (Arrays.asList("idMetVar").contains(actualToken.getDescription())) {
                metodo_atributoSinVisibilidad(tipo);
                //metodo
            }
            else {
                if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
                    //constructor
                    argsFormales();
                    bloque();
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
            argsFormales();
            bloque();
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
        bloque();
    }

    private void bloque() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("abreCorchete").contains(actualToken.getDescription())) {
            match("abreCorchete");
            listaSentencias();
            match("cierraCorchete");
        }
        else{
            //vacio
        }
    }

    private void listaSentencias() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("puntoComa","pr_this","idMetVar","pr_new","idClase", "abreParentesis","pr_var","pr_return","pr_if","pr_while","abreCorchete","pr_int","pr_boolean","pr_char").contains(actualToken.getDescription())) {
            sentencia();
            listaSentencias();
        }
        else{
            //vacio
        }
    }

    private void sentencia() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("puntoComa").contains(actualToken.getDescription())) {
            match("puntoComa");
        } else if (Arrays.asList("idClase").contains(actualToken.getDescription())) {
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            claseGenerica(tokensGenericos);
            if (Arrays.asList("punto").contains(actualToken.getDescription())){
                acceso();
                asignacion();
                match("puntoComa");
            }
            else{
                varLocalTipoClase();
            }
            
        } else if (Arrays.asList("pr_this","idMetVar","pr_new","idClase", "abreParentesis").contains(actualToken.getDescription())) {
            acceso();
            asignacion();
            match("puntoComa");
        } else if (Arrays.asList("pr_var","pr_int","pr_boolean","pr_char").contains(actualToken.getDescription())) {
            varLocal();
            match("puntoComa");
        } else if (Arrays.asList("pr_return").contains(actualToken.getDescription())) {
            retorno();
            match("puntoComa");
        } else if (Arrays.asList("pr_if").contains(actualToken.getDescription())) {
                If();
        } else if (Arrays.asList("pr_while").contains(actualToken.getDescription())) {
            While();
        } else if (Arrays.asList("abreCorchete").contains(actualToken.getDescription())) {
            bloque();
        }
        else {
            throw new SyntaxException(actualToken,"sentencia");
        }
    }

    private void varLocalTipoClase() throws LexicalException, SyntaxException, IOException, SemanticException {
        listaDecVars();
        match("=");
        expresion();
    }

    private void While() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_while");
        match("abreParentesis");
        expresion();
        match("cierraParentesis");
        sentencia();
    }

    private void If() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("pr_if");
        match("abreParentesis");
        expresion();
        match("cierraParentesis");
        sentencia();
        Else();
    }

    private void Else() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_else").contains(actualToken.getDescription())) {
            match("pr_else");
            sentencia();
        }
        else{
            //vacio
        }
    }

    private void retorno() throws LexicalException, SyntaxException, IOException {
        match("pr_return");
        expresionOpt();
    }

    private void expresionOpt() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("+","-","!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase").contains(actualToken.getDescription())) {
            expresion();
        }
        else{
            //vacio
        }
    }

    private void varLocal() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("pr_var").contains(actualToken.getDescription())) {
            match("pr_var");
            listaDecVars();
            match("=");
            expresion();
        }
        else{
            Tipo tipo =tipoPrimitivo();
            listaDecVars();
            match("=");
            expresion();
        }
    }


    private void asignacion() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("=","+=","-=").contains(actualToken.getDescription())) {
            tipoDeAsignacion();
            expresion();
        }
        else {

        }

    }

    private void expresion() throws LexicalException, SyntaxException, IOException {
        expresionUnaria();
        expresionPrima();
    }

    private void expresionPrima() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*",  "/", "%").contains(actualToken.getDescription())) {
            operadorBinario();
            expresionUnaria();
            expresionPrima();
        }
        else{
            //vacio
        }
    }

    private void operadorBinario() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("||").contains(actualToken.getDescription())) {
            match("||");
        } else if (Arrays.asList("&&").contains(actualToken.getDescription())) {
            match("&&");
        } else if (Arrays.asList("==").contains(actualToken.getDescription())) {
            match("==");
        } else if (Arrays.asList("!=").contains(actualToken.getDescription())) {
            match("!=");
        } else if (Arrays.asList("<").contains(actualToken.getDescription())) {
            match("<");
        } else if (Arrays.asList(">").contains(actualToken.getDescription())) {
            match(">");
        } else if (Arrays.asList("<=").contains(actualToken.getDescription())) {
            match("<=");
        }  else if (Arrays.asList(">=").contains(actualToken.getDescription())) {
            match(">=");
        } else if (Arrays.asList("+").contains(actualToken.getDescription())) {
            match("+");
        } else if (Arrays.asList("-").contains(actualToken.getDescription())) {
            match("-");
        } else if (Arrays.asList("*").contains(actualToken.getDescription())) {
            match("*");
        } else if (Arrays.asList("/").contains(actualToken.getDescription())) {
            match("/");
        } else if (Arrays.asList("%").contains(actualToken.getDescription())) {
            match("%");
        }
        else {
            throw new SyntaxException(actualToken,"operador binario");
        }
    }

    private void expresionUnaria() throws LexicalException, SyntaxException, IOException {
         if (Arrays.asList("+","-","!").contains(actualToken.getDescription())) {
            operadorUnario();
            operando();
         }else if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
             operando();
         }
         else {
             throw new SyntaxException(actualToken,"una expresion unaria");
         }

    }

    private void operando() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(actualToken.getDescription())) {
            literal();
        }else if (Arrays.asList("pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
            acceso();
        }
        else {
            throw new SyntaxException(actualToken,"un operando");
        }
    }

    private void literal() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_null").contains(actualToken.getDescription())) {
            match("pr_null");
        } else if (Arrays.asList("pr_true").contains(actualToken.getDescription())) {
            match("pr_true");
        } else if (Arrays.asList("pr_false").contains(actualToken.getDescription())) {
            match("pr_false");
        } else if (Arrays.asList("intLiteral").contains(actualToken.getDescription())) {
            match("intLiteral");
        } else if (Arrays.asList("charLiteral").contains(actualToken.getDescription())) {
            match("charLiteral");
        } else if (Arrays.asList("stringLiteral").contains(actualToken.getDescription())) {
            match("stringLiteral");
        }
        else {
            throw new SyntaxException(actualToken,"un literal");
        }
    }

    private void operadorUnario() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("+").contains(actualToken.getDescription())) {
            match("+");
        } else if (Arrays.asList("-").contains(actualToken.getDescription())) {
            match("-");
        } else if (Arrays.asList("!").contains(actualToken.getDescription())) {
            match("!");
        }
        else {
            throw new SyntaxException(actualToken,"un operador unario");
        }
    }

    private void tipoDeAsignacion() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("=").contains(actualToken.getDescription())){
            match("=");
        } else if (Arrays.asList("+=").contains(actualToken.getDescription())) {
            match("+=");
        } else if (Arrays.asList("-=").contains(actualToken.getDescription())) {
            match("-=");
        }
        else {
            throw new SyntaxException(actualToken,"una asignacion");
        }
    }

    private void acceso() throws LexicalException, SyntaxException, IOException {
        primario();
        encadenadoOpt();
    }

    private void encadenadoOpt() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            match("punto");
            match("idMetVar");
            encadenadoOptPrima();
        }
        else{

        }

    }

    private void encadenadoOptPrima() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            encadenadoOpt();
        } else if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            argsActuales();
            encadenadoOpt();
        } else{

        }
    }

    private void primario() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_this").contains(actualToken.getDescription())) {
            accesoThis();
        } else if (Arrays.asList("idMetVar").contains(actualToken.getDescription())) {
            match("idMetVar");
            accesoMetodo();
        } else if (Arrays.asList("pr_new").contains(actualToken.getDescription())) {
            accesoConstructor();
        } else if (Arrays.asList("punto").contains(actualToken.getDescription())) {
            accesoMetodoEstatico();
        } else if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            expresionParentizada();
        }
        else {
            throw new SyntaxException(actualToken,"un this,id de Metodo o variable, un new,un . o un (");
        }
    }

    private void expresionParentizada() throws LexicalException, SyntaxException, IOException {
        match("abreParentesis");
        expresion();
        match("cierraParentesis");
    }

    private void listaExpsOpt() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList( "+","-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "abreParentesis").contains(actualToken.getDescription())) {
            listaExps();
        }
        else{

        }


    }

    private void listaExps() throws LexicalException, SyntaxException, IOException {
        expresion();
        listaExpsPrima();
    }

    private void listaExpsPrima() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaExps();
        }
        else{

        }
    }

    private void accesoMetodoEstatico() throws LexicalException, SyntaxException, IOException {
        match("punto");
        match("idMetVar");
        argsActuales();
    }



    private void argsActuales() throws LexicalException, SyntaxException, IOException {
        match("abreParentesis");
        listaExpsOpt();
        match("cierraParentesis");
    }

    private void accesoMetodo() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
            argsActuales();
        }
        else{

        }
    }

    private void accesoConstructor() throws LexicalException, SyntaxException, IOException {
        match("pr_new");
        claseGenericaConstructor();
        argsActuales();
    }
    
    private void accesoThis() throws LexicalException, SyntaxException, IOException {
        match("pr_this");
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

    private void listaDecVars() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("idMetVar");
        listaDecVarsPrima();
    }

    private void listaDecVarsPrima() throws LexicalException, SyntaxException, IOException, SemanticException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaDecVars();
        }
        else{
            //vacio
        }
    }

    private Tipo tipo() throws LexicalException, SyntaxException, IOException {
        Tipo tipo;
        if (Arrays.asList("pr_boolean","pr_char","pr_int").contains(actualToken.getDescription())) {
            tipo=tipoPrimitivo();
        }
        else if(Arrays.asList("idClase").contains(actualToken.getDescription())){
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            tipo=new Tipo(actualToken);
            claseGenerica(tokensGenericos);
        }
        else {
            throw new SyntaxException(actualToken,"un tipo");
        }
        return tipo;
    }

    private Tipo tipoPrimitivo() throws LexicalException, SyntaxException, IOException {
        Tipo tipo;
        if (Arrays.asList("pr_boolean").contains(actualToken.getDescription())) {
            tipo=new Tipo(actualToken);
            match("pr_boolean");
        }
        else if(Arrays.asList("pr_char").contains(actualToken.getDescription())){
            tipo=new Tipo(actualToken);
            match("pr_char");
        }
        else if (Arrays.asList("pr_int").contains(actualToken.getDescription())){
            tipo=new Tipo(actualToken);
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

    private LinkedList<Token> implementaA() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_implements").contains(actualToken.getDescription())) {
            match("pr_implements");
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            return listaClaseGenerica(tokensGenericos);
        }
        else {
            return null;
        }
    }

    private void listaTipoReferencia() throws LexicalException, SyntaxException, IOException {
        match("idClase");
        listaTipoReferenciaPrima();
    }

    private void listaTipoReferenciaPrima() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            match("coma");
            listaTipoReferencia();
        }
        else {

        }
    }

    private Token heredaDe() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_extends").contains(actualToken.getDescription())) {
            match("pr_extends");
            LinkedList<Token> tokensGenericos=new LinkedList<>();
            LinkedList<Token>tokensGenericosNueva=claseGenerica(tokensGenericos);
            return tokensGenericosNueva.getFirst();
        }
        else{
            return new Token("idClase","Object", 0);//PReguntar
        }
    }

    private LinkedList<Token> listaClaseGenerica(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        LinkedList<Token> tokenGenericosActualizado= claseGenerica(tokensGenericos);
        return listaClaseGenericaPrima(tokenGenericosActualizado);
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
            LinkedList<Token> tokensGenericosActualizado=listaClaseGenerica(tokensGenericos);
            tokensGenericosActualizado.add(actualToken);
            match(">");
            return tokensGenericosActualizado;
        }
        else {
            return tokensGenericos;
        }
    }

    private LinkedList<Token> listaClaseGenericaPrima(LinkedList<Token> tokensGenericos) throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("coma").contains(actualToken.getDescription())) {
            tokensGenericos.add(actualToken);
            match("coma");
            LinkedList<Token> tokensGenericosActualizado=listaClaseGenerica(tokensGenericos);
            return tokensGenericosActualizado;
        }
        else{
            return tokensGenericos;
        }
    }

    private void listaClaseGenericaConstructor() throws LexicalException, SyntaxException, IOException {
        claseGenericaConstructor();
        listaClaseGenericaPrimaConstructor();
    }

    private void claseGenericaConstructor() throws LexicalException, SyntaxException, IOException {
        match("idClase");
        picoConstructor();
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
