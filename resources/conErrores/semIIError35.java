///[Error:mB1|6]
// Error Semantico en linea 6: El metodo estatico "mB1" no fue declarado en la clase "B".
class A {
    public A a;
    int mA1() {
        B.mB1();
    }
}

class B {
    public A a;
    int mB1(boolean p1, int p2){

    }
}

interface C{

}

class Init{
    static void main()
    { }
}

