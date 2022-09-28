package AnalizadorSintactico;

import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.LexicalException;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class SyntaxAnalyzerEtapa2 {


        private LexicalAnalyzer lexicalAnalyzer;
        private Token actualToken;

        public SyntaxAnalyzerEtapa2(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException {
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
            claseGenerica();
            extiendeA();
            match("abreCorchete");
            listaEncabezado();
            match("cierraCorchete");
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
            if (Arrays.asList("pr_static").contains(actualToken.getDescription())){
                estaticoOpt();
                tipoMetodo();
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

        private void tipoMetodo() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_boolean","pr_int","pr_char","idClase").contains(actualToken.getDescription())) {
                tipo();

            }
            else{
                match("pr_void");
            }
        }

        private void estaticoOpt() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_static").contains(actualToken.getDescription())) {
                match("pr_static");
            }
            else{

            }
        }

        private void extiendeA() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_extends").contains(actualToken.getDescription())) {
                match("pr_extends");
                listaClaseGenerica();
            }
            else{

            }
        }

        private void claseConcreta() throws LexicalException, SyntaxException, IOException, SemanticException {
            match("pr_class");
            claseGenerica();
            heredaDe();
            implementaA();
            match("abreCorchete");
            listaMiembro();
            match("cierraCorchete");

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
                claseGenerica();
                if (Arrays.asList("idMetVar").contains(actualToken.getDescription())) {
                    metodo_atributoSinVisibilidad();
                    //metodo
                }
                else {
                    if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
                        //constructor
                        argsFormales();
                        bloque();
                    }
                    else{
                        throw new SyntaxException(actualToken,"variable o {");
                    }
                }
            }
            else{
                tipo();
                metodo_atributoSinVisibilidad();
            }
        }

        private void metodo_atributoSinVisibilidad() throws LexicalException, SyntaxException, IOException, SemanticException {
            match("idMetVar");
            if (Arrays.asList("abreParentesis").contains(actualToken.getDescription())) {
                argsFormales();
                bloque();
            }
            else{
                listaDecAtrsSinVisibilidad();
                match("puntoComa");
            }


        }

        private void listaDecAtrsSinVisibilidad() throws LexicalException, SyntaxException, IOException, SemanticException {
            if (Arrays.asList("coma").contains(actualToken.getDescription())) {
                match("coma");
                listaDecAtrs();
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
                claseGenerica();
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
            if (Arrays.asList("=").contains(actualToken.getDescription())) {
                match("=");
                expresion();
            } else if (Arrays.asList(";").contains(actualToken.getDescription())) {
                match(";");
            }
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
                tipoPrimitivo();
                listaDecVars();
                if (Arrays.asList("=").contains(actualToken.getDescription())) {
                    match("=");
                    expresion();
                } else  {

                }
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
            } else if (Arrays.asList("idClase","punto").contains(actualToken.getDescription())) {
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
            if (Arrays.asList("idClase").contains(actualToken.getDescription())) {
                match("idClase");
                match("punto");
                match("idMetVar");
                argsActuales();
            } else if (Arrays.asList("punto").contains(actualToken.getDescription())) {
                match("punto");
                match("idMetVar");
                argsActuales();
            }
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
            visibilidad();
            tipo();
            listaDecAtrs();
            match("puntoComa");
        }

        private void listaDecAtrs() throws LexicalException, SyntaxException, IOException, SemanticException {
            match("idMetVar");
            listaDecAtrsPrima();
        }

        private void listaDecAtrsPrima() throws LexicalException, SyntaxException, IOException, SemanticException {
            if (Arrays.asList("coma").contains(actualToken.getDescription())) {
                match("coma");
                listaDecAtrs();
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

        private void tipo() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_boolean","pr_char","pr_int").contains(actualToken.getDescription())) {
                tipoPrimitivo();
            }
            else if(Arrays.asList("idClase").contains(actualToken.getDescription())){
                claseGenerica();
            }
            else {
                throw new SyntaxException(actualToken,"un tipo");
            }
        }

        private void tipoPrimitivo() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_boolean").contains(actualToken.getDescription())) {
                match("pr_boolean");
            }
            else if(Arrays.asList("pr_char").contains(actualToken.getDescription())){
                match("pr_char");
            }
            else if (Arrays.asList("pr_int").contains(actualToken.getDescription())){
                match("pr_int");
            }
            else {
                throw new SyntaxException(actualToken,"un tipo Primitivo");
            }
        }

        private void visibilidad() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_public").contains(actualToken.getDescription())) {
                match("pr_public");
            }
            else if(Arrays.asList("pr_private").contains(actualToken.getDescription())){
                match("pr_private");
            }
            else{
                throw new SyntaxException(actualToken,"un public o private");
            }
        }

        private void implementaA() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_implements").contains(actualToken.getDescription())) {
                match("pr_implements");
                listaClaseGenerica();
            }
            else {

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

        private void heredaDe() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("pr_extends").contains(actualToken.getDescription())) {
                match("pr_extends");
                claseGenerica();

            }
            else{
            }
        }

        private void listaClaseGenerica() throws LexicalException, SyntaxException, IOException {
            claseGenerica();
            listaClaseGenericaPrima();
        }


        private void claseGenerica() throws LexicalException, SyntaxException, IOException {
            match("idClase");
            pico();
        }

        private void pico() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("<").contains(actualToken.getDescription())) {
                match("<");
                listaClaseGenerica();
                match(">");
            }
            else {

            }
        }

        private void listaClaseGenericaPrima() throws LexicalException, SyntaxException, IOException {
            if (Arrays.asList("coma").contains(actualToken.getDescription())) {
                match("coma");
                listaClaseGenerica();

            }
            else{

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

