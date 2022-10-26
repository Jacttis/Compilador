///[Error:C|6]
// Error Semantico en linea 6: Acceso a metodo estatico de una interfaz
class A {
    public A a;
    int mA1() {
        C.mB1();
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

