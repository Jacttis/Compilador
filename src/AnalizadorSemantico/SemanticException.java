package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class SemanticException extends Exception {
        private Token actualToken;
        private String error;

        public SemanticException(Token actualToken, String error) {
            this.actualToken = actualToken;
            this.error = error;
        }

        public void printStackTrace(){
            System.out.println(error);
            System.out.println("[Error:"+actualToken.getLexeme()+"|"+actualToken.getNumberline()+"]");
        }
}

