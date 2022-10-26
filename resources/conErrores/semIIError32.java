///[Error:Z|6]
// Error Semantico en linea 6: No es posible acceder al metodo "mB1" la clase "Z" no esta declarada.
class A {
    public A a;
    int mA1() {
        Z.mB1();
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

