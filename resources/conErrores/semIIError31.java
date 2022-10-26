///[Error:C|6]
//  Error Semantico en linea 6: No es posible crear un objeto del tipo de una interface
class A {
    public A a;
    int mA1() {
        var b2 = new C();
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

