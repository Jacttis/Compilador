///[Error:mB1|6]
// Error Semantico en linea 6: La cantidad de argumentos actuales no coincide con la cantidad de argumentos formales.
class A {
    public A a;
    int mA1() {
        B.mB1();
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

