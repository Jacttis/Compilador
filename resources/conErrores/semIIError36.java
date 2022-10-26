///[Error:mB1|6]
// Error Semantico en linea 6: El tipo de la expresion correspondiente al argumento actual "true" no coincide con el tipo del parametro formal.
class A {
    public A a;
    int mA1() {
        B.mB1(true,true);
    }
}

class B {
    public A a;
    static int mB1(boolean p1, int p2){

    }
}

interface C{

}

class Init{
    static void main()
    { }
}




